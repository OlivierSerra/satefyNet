package com.mariemoore.safetynet.controller;

import com.mariemoore.safetynet.dto.PersonMedicalDataDTO;
import com.mariemoore.safetynet.model.Firestation;
import com.mariemoore.safetynet.model.MedicalRecord;
import com.mariemoore.safetynet.model.Person;
import com.mariemoore.safetynet.service.FirestationService;
import com.mariemoore.safetynet.service.MedicalRecordService;
import com.mariemoore.safetynet.service.PersonService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(controllers = SafetyNetController.class)
public class SafetyNetControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    PersonService personService;

    @MockBean
    MedicalRecordService medicalRecordService;

    @MockBean
    FirestationService firestationService;

    private List<Person> personList;
    private Person john;
    private Person jacob;
    private Person tenley;

    private List<MedicalRecord> medicalRecordList;

    private MedicalRecord johnsMedicalRecord;
    private MedicalRecord jacobsMedicalRecord;
    private MedicalRecord tenleyMedicalRecord;

    private List<Firestation> firestationList;

    private Firestation firstFirestation;
    private Firestation secondFirestation;
    private Firestation thirdFirestation;

    @BeforeAll
    void setup() {
        john = new Person(
                "John",
                "Boyd",
                "1509 Culver St",
                "Culver",
                "97451",
                "841-874-6512",
                "jaboyd@email.com");
        jacob = new Person(
                "Jacob",
                "Boyd",
                "1509 Culver St",
                "Culver",
                "97451",
                "841-874-6513",
                "drk@email.com");

        tenley = new Person(
                "Tenley",
                "Boyd",
                "1509 Culver St",
                "Culver",
                "97451",
                "841-874-6512",
                "tenz@email.com");

        personList = Arrays.asList(john, jacob, tenley);
        johnsMedicalRecord = new MedicalRecord(
                "John",
                "Boyd",
                "03/06/1984",
                Arrays.asList("aznol:350mg", "hydrapermazol:100mg"),
                Arrays.asList("nillacilan"));
        jacobsMedicalRecord = new MedicalRecord(
                "Jacob",
                "Boyd",
                "03/06/1989",
                Arrays.asList("pharmacol:5000mg", "terazine:10mg", "noznazol:250mg"),
                Arrays.asList());

        tenleyMedicalRecord = new MedicalRecord(
                "Tenley",
                "Boyd",
                "02/18/2012",
                Arrays.asList(),
                Arrays.asList("peanut"));

        medicalRecordList = Arrays.asList(johnsMedicalRecord, jacobsMedicalRecord, tenleyMedicalRecord);

        firstFirestation = new Firestation(
                3,
                "1509 Culver St");
        secondFirestation = new Firestation(
                2,
                "29 15th St");

        thirdFirestation = new Firestation(
                3,
                "834 Binoc Ave"
        );

        firestationList = Arrays.asList(firstFirestation, secondFirestation, thirdFirestation);
    }

    @Test
    public void getPersonsAttachedToStationShouldReturnOk() throws Exception{
        when(firestationService.findAddressesOfFirestation(3)).thenReturn(Arrays.asList(
                "1509 Culver St",
                "834 Binoc Ave",
                "748 Townings Dr",
                "112 Steppes Pl",
                "748 Townings Dr"));
        mvc.perform(get("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .param("stationNumber", firstFirestation.getStation().toString()))
                .andExpect(status().isOk());
    }

    @Test
    public void getPersonsAttachedToNonexistantStationShouldReturnNoContent() throws Exception{
        when(firestationService.findAddressesOfFirestation(33)).thenReturn(Arrays.asList());
        mvc.perform(get("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("stationNumber", firstFirestation.getStation().toString()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getChildrenWithHouseholdAtAddressShouldReturnOk() throws Exception{
        // Mock the service methods
        when(personService.findPersonsByAddress(john.getAddress())).thenReturn(personList);
        when(medicalRecordService.getMedicalRecords()).thenReturn(medicalRecordList);
        when(personService.getHouseholdOfChild(john.getFirstName(), john.getLastName())).thenReturn(personList);

        mvc.perform(get("/childAlert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("address", firstFirestation.getAddress()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    public void getNoChildrenWithHouseholdAtAddressShouldReturnNoContent() throws Exception{
        when(personService.findPersonsByAddress("1509 Culver St")).thenReturn(Arrays.asList());
        mvc.perform(get("/childAlert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("address", firstFirestation.getAddress()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getPhoneNumbersAttachedToFirestationNumberShouldReturnOk() throws Exception{
        when(firestationService.findAddressesOfFirestation(3)).thenReturn(Arrays.asList(
                "1509 Culver St",
                "834 Binoc Ave",
                "748 Townings Dr",
                "112 Steppes Pl",
                "748 Townings Dr"));
        mvc.perform(get("/phoneAlert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("firestation", firstFirestation.getStation().toString()))
                .andExpect(status().isOk());
    }

    @Test
    public void getPhoneNumbersAttachedToNonexistentFirestationNumberShouldReturnNoContent() throws Exception{
        when(firestationService.findAddressesOfFirestation(3)).thenReturn(Arrays.asList());
        mvc.perform(get("/phoneAlert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("firestation", firstFirestation.getStation().toString()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getPeopleLivingAtAddressShouldReturnOk() throws Exception{
        when(personService.findPersonsByAddress(john.getAddress())).thenReturn(List.of(john));
        when(firestationService.findStationNumberByAddress(john.getAddress())).thenReturn(firstFirestation.getStation());
        when(medicalRecordService.getBirthdayFromFirstnameAndLastname(john.getFirstName(), john.getLastName())).thenReturn(johnsMedicalRecord.getBirthdate());


        List<PersonMedicalDataDTO> personMedicalDataDTOS = new ArrayList<>();
        HashMap<Integer, List<PersonMedicalDataDTO>> expectedResult = new HashMap<>();
        expectedResult.put(firstFirestation.getStation(), personMedicalDataDTOS);

        mvc.perform(get("/fire")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("address", firstFirestation.getAddress()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$.3").isArray());
    }

    @Test
    public void getNoOneLivingAtAddressShouldReturnNoContent() throws Exception{
        when(personService.findPersonsByAddress("1509 Culver St")).thenReturn(Arrays.asList());
        mvc.perform(get("/fire")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("address", firstFirestation.getStation().toString()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getHouseholdAttachedToFirestationShouldReturnOk() throws Exception{
        when(firestationService.getFirestations()).thenReturn(firestationList);
        when(personService.findPersonsByAddress(john.getAddress())).thenReturn(personList); // Mock your person data
        when(medicalRecordService.getBirthdayFromFirstnameAndLastname(john.getFirstName(), john.getLastName())).thenReturn(johnsMedicalRecord.getBirthdate());

        HashMap<String, List<PersonMedicalDataDTO>> expectedResult = new HashMap<>();
        List<PersonMedicalDataDTO> persons = new ArrayList<>();
        expectedResult.put(firstFirestation.getAddress(), persons);
        ArrayList<Integer> stationNumbers = new ArrayList<>(Arrays.asList(1, 2));

        mvc.perform(get("/flood/stations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("stations", stationNumbers.stream().map(Object::toString).toArray(String[]::new)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(expectedResult.size()));

    }

    @Test
    public void getNoHouseholdAttachedToFirestationShouldReturnNoContent() throws Exception{
        when(firestationService.getFirestations()).thenReturn(new ArrayList<>());
        ArrayList<Integer> stationNumbers = new ArrayList<>(Arrays.asList(100, 200));

        mvc.perform(get("/flood/stations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("stations", stationNumbers.stream().map(Object::toString).toArray(String[]::new)))
                .andExpect(status().isNoContent());

    }

    @Test
    void testGetListOfPersonsShouldReturnOk() throws Exception {
        when(personService.getPersons()).thenReturn(personList);
        when(medicalRecordService.getBirthdayFromFirstnameAndLastname(john.getFirstName(), john.getLastName())).thenReturn(johnsMedicalRecord.getBirthdate());
        when(medicalRecordService.getMedicationFromFirstnameAndLastname(john.getFirstName(), john.getLastName())).thenReturn(johnsMedicalRecord.getMedications());
        when(medicalRecordService.getAllergiesFromFirstnameAndLastname(john.getFirstName(), john.getLastName())).thenReturn(johnsMedicalRecord.getAllergies());

        // Perform the request and validate the response
        mvc.perform(get("/personInfo")
                        .param("firstName", john.getFirstName())
                        .param("lastName", john.getLastName())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].firstName").value(john.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(john.getLastName()))
                .andExpect(jsonPath("$[0].address").value(john.getAddress()))
                .andExpect(jsonPath("$[0].age").value(39)) // Assuming current date is 2023-06-02
                .andExpect(jsonPath("$[0].email").value(john.getEmail()))
                .andExpect(jsonPath("$[0].medications").isArray())
                .andExpect(jsonPath("$[0].allergies").isArray());
    }
    @Test
    void testGetListOfPersonsWithPersonNotFoundShouldReturnNoContent() throws Exception {
        when(personService.getPersons()).thenReturn(new ArrayList<>());
        mvc.perform(get("/personInfo")
                        .param("firstName", john.getFirstName())
                        .param("lastName", john.getLastName())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetPersonsEmailsByCityShouldReturnOk() throws Exception {
        when(personService.getPersons()).thenReturn(personList);

        // Perform the request and validate the response
        mvc.perform(get("/communityEmail")
                        .param("city", john.getCity())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetPersonsEmailsByCityWhenCityNotFoundShouldReturnNoContent() throws Exception {
        when(personService.getPersons()).thenReturn(new ArrayList<>());

        // Perform the request and validate the response
        mvc.perform(get("/communityEmail")
                        .param("city", john.getCity())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }


}
