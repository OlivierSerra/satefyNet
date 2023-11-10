package com.mariemoore.safetynet.controller;

import com.mariemoore.safetynet.dto.*;
import com.mariemoore.safetynet.model.Person;
import com.mariemoore.safetynet.service.FirestationService;
import com.mariemoore.safetynet.service.MedicalRecordService;
import com.mariemoore.safetynet.service.PersonService;
import com.mariemoore.safetynet.utils.Calculations;
import com.mariemoore.safetynet.utils.CustomFilters;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.apache.logging.log4j.LogManager.getLogger;

@RestController
public class SafetyNetController {
    private static final Logger logger = getLogger(MedicalRecordController.class);
    @Autowired
    private PersonService personService;
    @Autowired
    private FirestationService firestationService;
    @Autowired
    private MedicalRecordService medicalRecordService;


    public SafetyNetController(PersonService personService, FirestationService firestationService, MedicalRecordService medicalRecordService){
        this.personService = personService;
        this.firestationService = firestationService;
        this.medicalRecordService = medicalRecordService;
    }

    @ResponseBody
    @GetMapping("/firestation")
    public ResponseEntity<PersonAndFirestationDTO> getPersonsAttachedToStation(@RequestParam(value = "stationNumber") Integer stationNumber){
        List<String> stationAddresses = this.firestationService.findAddressesOfFirestation(stationNumber);
        List<PersonPhoneDTO> personsDTO = null;
        HashMap<String, Integer> nbOfAdultsAndChildren = null;

        if (stationAddresses.size() > 0) {
            personsDTO = this.personService.getPersons().stream()
                    .filter(person -> stationAddresses.stream().anyMatch(a -> Objects.equals(person.getAddress(), a)))
                    .map(p-> new PersonPhoneDTO(p.getFirstName(), p.getLastName(), p.getPhone())).collect(Collectors.toList());
            nbOfAdultsAndChildren = Calculations.countAdultsAndChildren(personsDTO , this.medicalRecordService.getMedicalRecords());
        }
        else {
            logger.error("could not get persons who are attached to station " + stationNumber);
            return ResponseEntity.noContent().build();
        }

        PersonAndFirestationDTO result = new PersonAndFirestationDTO(personsDTO, nbOfAdultsAndChildren);
        logger.info("getting all persons attached to firestation number");
        return ResponseEntity.ok().body(result);
    }

    @ResponseBody
    @GetMapping("/childAlert")
    public ResponseEntity<List<ChildWithHouseholdDTO>> getChildrenWithHouseholdAtAddress(@RequestParam(value = "address") String address){
        //get all people living at this address
        List<Person> peopleLivingAtAddress = this.personService.findPersonsByAddress(address);
        if(peopleLivingAtAddress.size() == 0 ){
            logger.error("could not find anyone living at this address: " + address);
            return ResponseEntity.noContent().build();
        }

        //get all children out of this list with their age
        List<PersonAgeDTO> children = CustomFilters.getChildrenFromList(peopleLivingAtAddress, this.medicalRecordService.getMedicalRecords());

        //get all children with their household
        List<ChildWithHouseholdDTO> childrenWithAge = children.stream()
                        .map(child -> new ChildWithHouseholdDTO(
                                child.getFirstName(),
                                child.getLastName(),
                                child.getAge(),
                                personService.getHouseholdOfChild(
                                        child.getFirstName(),
                                        child.getLastName())
                        ))
                        .collect(Collectors.toList());
        logger.info("getting all children and their household attached to address");
        return ResponseEntity.ok().body(childrenWithAge);
    }

    @ResponseBody
    @GetMapping("/phoneAlert")
    public ResponseEntity<List<String>> getPhoneNumbersAttachedToFirestationNumber(@RequestParam(value="firestation") Integer stationNumber){
        List<String> stationAddresses = this.firestationService.findAddressesOfFirestation(stationNumber);

        if(stationAddresses.size() == 0){
            logger.error("could not find anyone attached to this firestation number: " + stationNumber);
            return ResponseEntity.noContent().build();
        }
        List<String> phoneNumbers = this.personService.getPersons().stream()
                .filter(person -> stationAddresses.stream().anyMatch(address -> Objects.equals(person.getAddress(), address)))
                .map(person -> person.getPhone())
                .collect(Collectors.toList());
        logger.info("getting phone numbers of everybody attached to firestation number");
        return ResponseEntity.ok().body(phoneNumbers);
    }

    @ResponseBody
    @GetMapping("/fire")
    public ResponseEntity<HashMap<Integer, List<PersonMedicalDataDTO>>> getPeopleLivingAtAddress(@RequestParam(value="address") String address){
        List<Person> personList = this.personService.findPersonsByAddress(address);
        Integer stationNumber;
        List<PersonMedicalDataDTO> personMedicalDataDTOS;
        HashMap <Integer, List<PersonMedicalDataDTO>> result = new HashMap<>();
        if(personList.size() == 0){
            logger.error("could not find anyone living at this address: " + address);
            return ResponseEntity.noContent().build();
        }
        stationNumber = this.firestationService.findStationNumberByAddress(address);
        //List DTO (firstname, lastname, phone, age, medications, allergies
        personMedicalDataDTOS = personList.stream()
                .map(person -> new PersonMedicalDataDTO(
                        person.getFirstName(),
                        person.getLastName(),
                        person.getPhone(),
                        Calculations.calculateAgeFromBirthday(
                                this.medicalRecordService.getBirthdayFromFirstnameAndLastname(
                                        person.getFirstName(), person.getLastName()
                                )
                        ),
                        this.medicalRecordService.getMedicationFromFirstnameAndLastname(
                                person.getFirstName(), person.getLastName()
                        ),
                        this.medicalRecordService.getAllergiesFromFirstnameAndLastname(
                                person.getFirstName(), person.getLastName()
                        )
                ))
                .collect(Collectors.toList());
        result.put(stationNumber, personMedicalDataDTOS);
        logger.info("getting people living at address");
        return ResponseEntity.ok().body(result);
    }

    @ResponseBody
    @GetMapping("/flood/stations")
    public ResponseEntity<HashMap<String, List<PersonMedicalDataDTO>> > getHouseholdAttachedToFirestation(@RequestParam("stations") List<Integer> stationNumbers){
        //list of all addresses of station numbers
        List<String> allAddresses = this.firestationService.getFirestations().stream()
                        .filter(firestation -> stationNumbers.stream()
                                .anyMatch(number -> Objects.equals(firestation.getStation(), number))
                        )
                        .map(firestation -> firestation.getAddress())
                        .collect(Collectors.toList());

        if(allAddresses.size() == 0 ){
            logger.error("could not find anyone household attached to this list of firestation numbers: " + stationNumbers);
            return ResponseEntity.noContent().build();
        }
        //List of PersonMedicalDataDTO (firstname, lastname, phone, age, medications, allergies)
        HashMap<String, List<PersonMedicalDataDTO>> result = new HashMap<>();

        for(String address: allAddresses){
            List<PersonMedicalDataDTO> persons = this.personService.findPersonsByAddress(address).stream()
                    .map(person -> new PersonMedicalDataDTO(
                            person.getFirstName(),
                            person.getLastName(),
                            person.getPhone(),
                            Calculations.calculateAgeFromBirthday(
                                    this.medicalRecordService.getBirthdayFromFirstnameAndLastname(
                                            person.getFirstName(), person.getLastName()
                                    )
                            ),
                            this.medicalRecordService.getMedicationFromFirstnameAndLastname(
                                    person.getFirstName(), person.getLastName()
                            ),
                            this.medicalRecordService.getAllergiesFromFirstnameAndLastname(
                                    person.getFirstName(), person.getLastName()
                            )

                    ))
                    .collect(Collectors.toList());
            result.put(address, persons);
        }
        logger.info("getting household attached to firestation numbers ");
        return ResponseEntity.ok().body(result);
    }

    @ResponseBody
    @GetMapping("/personInfo")
    public ResponseEntity<List<PersonDataMedicalDataDTO>> getListOfPersons(@RequestParam(value="firstName") String firstName, @RequestParam(value="lastName") String lastName){
        //List DTO (firstname, lastname, address, age, email, medications, allergies
        List<PersonDataMedicalDataDTO> persons = this.personService.getPersons().stream()
                        .filter(person -> Objects.equals(person.getFirstName(), firstName) &&
                                Objects.equals(person.getLastName(), lastName))
                        .map(person -> new PersonDataMedicalDataDTO(
                                        person.getFirstName(),
                                        person.getLastName(),
                                        person.getAddress(),
                                        Calculations.calculateAgeFromBirthday(
                                                this.medicalRecordService.getBirthdayFromFirstnameAndLastname(
                                                        person.getFirstName(), person.getLastName()
                                                )
                                        ),
                                        person.getEmail(),
                                        this.medicalRecordService.getMedicationFromFirstnameAndLastname(
                                                person.getFirstName(), person.getLastName()
                                        ),
                                        this.medicalRecordService.getAllergiesFromFirstnameAndLastname(
                                                person.getFirstName(), person.getLastName()
                                        )
                                ))
                                        .collect(Collectors.toList());
        if(persons.size() == 0){
            logger.error("could not get a list of persons with this firstname and lastname");
            return ResponseEntity.noContent().build();
        }
        logger.info("getting all persons info by first and last names");
        return ResponseEntity.ok().body(persons);
    }

     @ResponseBody
     @GetMapping("/communityEmail")
     public ResponseEntity<List<String>> getPersonsEmailsByCity(@RequestParam(value="city") String city){
         List<String> emails = this.personService.getPersons().stream()
                     .filter(person -> Objects.equals(person.getCity(), city))
                     .map(person -> person.getEmail())
                 .distinct()
                 .collect(Collectors.toList());
         if(emails.size() == 0){
             logger.error("could not get a list of persons living in this city: " + city);
             return ResponseEntity.noContent().build();
         }
         logger.info("getting emails of all persons living at: " + city);
         return ResponseEntity.ok().body(emails);
     }
}
