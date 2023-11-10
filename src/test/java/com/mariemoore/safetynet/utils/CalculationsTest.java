package com.mariemoore.safetynet.utils;

import com.mariemoore.safetynet.dto.PersonPhoneDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import com.mariemoore.safetynet.model.MedicalRecord;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CalculationsTest {
    private PersonPhoneDTO john;
    private PersonPhoneDTO jacob;
    private PersonPhoneDTO tenley;
    private MedicalRecord johnsMedicalRecord;
    private MedicalRecord jacobsMedicalRecord;
    private MedicalRecord tenleyMedicalRecord;

    private List<PersonPhoneDTO> personsDTOList;
    private List<MedicalRecord> medicalRecordList;
    @BeforeAll
    void setup() {
        john = new PersonPhoneDTO(
                "John",
                "Boyd",
                "841-874-6512");
        jacob = new PersonPhoneDTO(
                "Jacob",
                "Boyd",
                "841-874-6513");

        tenley = new PersonPhoneDTO(
                "Tenley",
                "Boyd",
                "841-874-6512");

        personsDTOList = Arrays.asList(john, jacob, tenley);
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
    }

    @Test
    public void calculateAgeFromBirthdayShouldReturnAge(){
        long ageOne = Calculations.calculateAgeFromBirthday("03/06/1984");
        long ageTwo = Calculations.calculateAgeFromBirthday("02/18/2012");
        long ageThree = Calculations.calculateAgeFromBirthday("03/06/1989");
        Assertions.assertEquals(39, ageOne);
        Assertions.assertEquals(11, ageTwo);
        Assertions.assertEquals(34, ageThree);
    }

    @Test
    public void countAdultsAndChildrenShouldReturnOk(){
        HashMap<String, Integer> adultChildrenCounts = Calculations.countAdultsAndChildren(personsDTOList, medicalRecordList);
        Assertions.assertEquals(2, adultChildrenCounts.get("adultCount"));
        Assertions.assertEquals(1, adultChildrenCounts.get("childCount"));
    }
}
