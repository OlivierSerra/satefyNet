package com.mariemoore.safetynet.utils;

import com.mariemoore.safetynet.dto.PersonAgeDTO;
import com.mariemoore.safetynet.model.MedicalRecord;
import com.mariemoore.safetynet.model.Person;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CustomFilters {
    public static List<PersonAgeDTO> getChildrenFromList(List<Person> list, List<MedicalRecord> medicalRecords){
        List<PersonAgeDTO> childrenWithAge = new ArrayList<>();

        for(Person child: list){
            //loop through the children to get firstname and lastname
            for(MedicalRecord medicalRecord: medicalRecords){
                //loop through medical records to get birthdate and calculate it
                PersonAgeDTO personAgeDTO = new PersonAgeDTO();
                if(Objects.equals(child.getFirstName(), medicalRecord.getFirstName()) &&
                        Objects.equals(child.getLastName(), medicalRecord.getLastName()) &&
                        Calculations.calculateAgeFromBirthday(medicalRecord.getBirthdate()) < 18){
                    personAgeDTO.setFirstName(child.getFirstName());
                    personAgeDTO.setLastName(child.getLastName());
                    personAgeDTO.setAge(Calculations.calculateAgeFromBirthday(medicalRecord.getBirthdate()));
                    childrenWithAge.add(personAgeDTO);
                }
            }
        }
        return childrenWithAge;
    }
}
