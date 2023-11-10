package com.mariemoore.safetynet.repository;


import com.mariemoore.safetynet.model.MedicalRecord;
import com.mariemoore.safetynet.utils.Validation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(MedicalRecordRepository.class)
public class MedicalRepositoryTest {

    @Autowired
    MedicalRecordRepository medicalRecordRepository;

    @MockBean
    JsonDataGetter dataGetter;

    @MockBean
    private Validation validation;
    private List<MedicalRecord> medicalRecordList;

    private MedicalRecord johnsMedicalRecord;
    private MedicalRecord jacobsMedicalRecord;
    private MedicalRecord tenleyMedicalRecord;
    @BeforeAll
    void setUp(){
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
        medicalRecordRepository = new MedicalRecordRepository(dataGetter, validation);
    }

    @Test
    public void findAllSuccessTest (){
        medicalRecordRepository.medicalRecords = medicalRecordList;
        List<MedicalRecord> result = medicalRecordRepository.findAll();
        Assertions.assertEquals(3, result.size());
    }

    @Test
    public void saveMedicalRecordShouldReturnOk() {
        MedicalRecord  newMedicalRecord = new MedicalRecord(
                "John",
                "Doe",
                "03/06/1984",
                Arrays.asList("aznol:350mg", "hydrapermazol:100mg"),
                Arrays.asList("nillacilan"));
        medicalRecordRepository.medicalRecords = new ArrayList<>(dataGetter.getMedicalRecordsData());
        medicalRecordRepository.save(newMedicalRecord);
        MedicalRecord result = medicalRecordRepository.findMedicalRecordByFirstnameAndLastname("John","Doe");
        Assertions.assertEquals("John", result.getFirstName());
        assertNotNull(result);
    }

    @Test
    public void saveInvalidMedicalRecordShouldReturnReturnNull() {
        MedicalRecord  invalidMedicalRecord = new MedicalRecord(
                "John",
                "",
                "03/06/1984",
                Arrays.asList("aznol:350mg", "hydrapermazol:100mg"),
                Arrays.asList("nillacilan"));
        medicalRecordRepository.medicalRecords = new ArrayList<>(dataGetter.getMedicalRecordsData());
        MedicalRecord result = medicalRecordRepository.save(invalidMedicalRecord);
        Assertions.assertEquals(null, result);
    }

    @Test
    public void updateMedicalRecordShouldReturnOk() {
        MedicalRecord updatedJohnsMedicalRecord = new MedicalRecord(
                "John",
                "Boyd",
                "03/06/1988",
                Arrays.asList("aznol:350mg", "hydrapermazol:100mg"),
                Arrays.asList("nillacilan"));
        medicalRecordRepository.medicalRecords = medicalRecordList;
        MedicalRecord result = medicalRecordRepository.findMedicalRecordByFirstnameAndLastname("John","Boyd");
        Assertions.assertEquals("03/06/1984", result.getBirthdate());
        medicalRecordRepository.update(updatedJohnsMedicalRecord);
        MedicalRecord updatedResult = medicalRecordRepository.findMedicalRecordByFirstnameAndLastname("John","Boyd");
        Assertions.assertEquals("03/06/1988", updatedResult.getBirthdate());
        assertNotNull(result);
    }

    @Test
    public void updateUnknownMedicalRecordShouldReturnNull() {
        MedicalRecord unknownMedicalRecord = new MedicalRecord(
                "Johnny",
                "Bower",
                "03/06/1988",
                Arrays.asList("aznol:350mg", "hydrapermazol:100mg"),
                Arrays.asList("nillacilan"));
        medicalRecordRepository.medicalRecords = medicalRecordList;
        MedicalRecord result = medicalRecordRepository.update(unknownMedicalRecord);
        Assertions.assertEquals(null, result);
    }

    @Test
    public void deleteMedicalRecordShouldReturnOk() {
        MedicalRecord toSave = new MedicalRecord(
                "Marie",
                "Moore",
                "03/06/1984",
                Arrays.asList("aznol:350mg", "hydrapermazol:100mg"),
                Arrays.asList("nillacilan"));
        medicalRecordRepository.save(toSave);
        MedicalRecord newlySaved = medicalRecordRepository.findMedicalRecordByFirstnameAndLastname("Marie","Moore");
        Assertions.assertEquals("Marie", newlySaved.getFirstName());
        medicalRecordRepository.delete(toSave);
        MedicalRecord deleted = medicalRecordRepository.findMedicalRecordByFirstnameAndLastname("Marie","Moore");
        Assertions.assertEquals(null, deleted);
    }

    @Test
    public void deleteUnknownMedicalRecordShouldReturnNull() {
        MedicalRecord unknownMedicalRecord = new MedicalRecord(
                "Johnny",
                "Bower",
                "03/06/1988",
                Arrays.asList("aznol:350mg", "hydrapermazol:100mg"),
                Arrays.asList("nillacilan"));
        MedicalRecord result = medicalRecordRepository.delete(unknownMedicalRecord);
        Assertions.assertEquals(null, result);
    }

    @Test
    public void findBirthdayByFirstnameAndLastNameShouldReturnMedicalRecord(){
        medicalRecordRepository.medicalRecords = medicalRecordList;
        String birthday = medicalRecordRepository.getBirthdayFromFirstnameAndLastname("John", "Boyd");
        Assertions.assertEquals("03/06/1988", birthday);
    }

    @Test
    public void findBirthdayOfNonExistingPersonShouldReturnNull(){
        medicalRecordRepository.medicalRecords = medicalRecordList;
        String birthday = medicalRecordRepository.getBirthdayFromFirstnameAndLastname("Marie", "Moore");
        Assertions.assertEquals(null, birthday);
    }

    @Test
    public void getMedicationFromFirstnameAndLastnameShouldReturnMedication() {
        medicalRecordRepository.medicalRecords = medicalRecordList;
        List<String> medication = medicalRecordRepository.getMedicationFromFirstnameAndLastname("John", "Boyd");
        Assertions.assertEquals("aznol:350mg", medication.get(0));
    }

    @Test
    public void getMedicationFromNonExistantPersonShouldReturnNull() {
        medicalRecordRepository.medicalRecords = medicalRecordList;
        List<String> medication = medicalRecordRepository.getMedicationFromFirstnameAndLastname("Marie", "Moore");
        Assertions.assertEquals(null, medication);
    }

    @Test
    public void getAllergiesFromFirstnameAndLastnameShouldReturnAllergies() {
        medicalRecordRepository.medicalRecords = medicalRecordList;
        List<String> allergies = medicalRecordRepository.getAllergiesFromFirstnameAndLastname("John", "Boyd");
        Assertions.assertEquals("nillacilan", allergies.get(0));
    }

    @Test
    public void getAllergiesFromNonExistantPersonShouldReturnNull() {
        medicalRecordRepository.medicalRecords = medicalRecordList;
        List<String> allergies = medicalRecordRepository.getAllergiesFromFirstnameAndLastname("Marie", "Moore");
        Assertions.assertEquals(null, allergies);
    }
}
