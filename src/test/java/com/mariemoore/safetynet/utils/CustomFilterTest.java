package com.mariemoore.safetynet.utils;

import com.mariemoore.safetynet.dto.PersonAgeDTO;
import com.mariemoore.safetynet.model.MedicalRecord;
import com.mariemoore.safetynet.model.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CustomFilterTest {
    private List<Person> personList;
    private Person john;
    private Person jacob;
    private Person tenley;
    private MedicalRecord johnsMedicalRecord;
    private MedicalRecord jacobsMedicalRecord;
    private MedicalRecord tenleyMedicalRecord;
    private List<MedicalRecord> medicalRecordList;

    @Test
    public void getChildrenFromListShouldReturnOk(){
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

        List<PersonAgeDTO> childrenWithAge = CustomFilters.getChildrenFromList(personList, medicalRecordList);
        Assertions.assertEquals(1, childrenWithAge.size());
    }
}
