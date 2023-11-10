package com.mariemoore.safetynet.utils;

import com.mariemoore.safetynet.model.Firestation;
import com.mariemoore.safetynet.model.MedicalRecord;
import com.mariemoore.safetynet.model.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ValidationTest {
    private Person john;
    private Person jacob;
    private MedicalRecord johnsMedicalRecord;
    private MedicalRecord jacobsMedicalRecord;
    private Firestation firstFirestation;
    private Firestation secondFirestation;

    List<MedicalRecord> medicalRecordList;

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
                "",
                "Culver",
                "97451",
                "841-874-6513",
                "drk@email.com");

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
                null);

        firstFirestation = new Firestation(
                3,
                "1509 Culver St");
        secondFirestation = new Firestation(
                2,
                "");

        medicalRecordList = Arrays.asList(johnsMedicalRecord, jacobsMedicalRecord);


    }
    @Test
    public void isPersonInvalidShouldReturnFalseWhenPersonIsValid(){
        boolean result = Validation.isPersonInvalid(john);
        Assertions.assertEquals(false, result);
    }

    @Test
    public void isPersonInvalidShouldReturnTrueWhenPersonIsNotValid(){
        boolean result = Validation.isPersonInvalid(jacob);
        Assertions.assertEquals(true, result);
    }

    @Test
    public void isFireStationInvalidShouldReturnFalseWhenFireStationIsValid(){
        boolean result = Validation.isFireStationInvalid(firstFirestation);
        Assertions.assertEquals(false, result);
    }

    @Test
    public void isFireStationInvalidShouldReturnTrueWhenFireStationIsNotValid(){
        boolean result = Validation.isFireStationInvalid(secondFirestation);
        Assertions.assertEquals(true, result);
    }

    @Test
    public void isMedicalRecordInvalidShouldReturnFalseWhenMedicalRecordIsValid(){
        boolean result = Validation.isMedicalRecordInvalid(johnsMedicalRecord);
        Assertions.assertEquals(false, result);
    }

    @Test
    public void isMedicalRecordInvalidShouldReturnTrueWhenMedicalRecordIsNotValid(){
        boolean result = Validation.isMedicalRecordInvalid(jacobsMedicalRecord);
        Assertions.assertEquals(true, result);
    }

    @Test
    public void medicalRecordExistsShouldReturnMedicalRecordWhenItExistsByFirstAndLastName(){
        MedicalRecord medicalRecord = Validation.medicalRecordExists(medicalRecordList, john.getFirstName(), john.getLastName());
        Assertions.assertEquals(johnsMedicalRecord, medicalRecord);
    }

    @Test
    public void medicalRecordExistsShouldReturnNullWhenItDoesNotExistByFirstAndLastName(){
        MedicalRecord medicalRecord = Validation.medicalRecordExists(medicalRecordList, "John", "Doe");
        Assertions.assertEquals(null, medicalRecord);
    }
}






