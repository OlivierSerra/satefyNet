package com.mariemoore.safetynet.service;

import com.mariemoore.safetynet.model.MedicalRecord;
import com.mariemoore.safetynet.repository.MedicalRecordRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public List<MedicalRecord> getMedicalRecords(){
        return medicalRecordRepository.findAll();
    }

    public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord){
        return this.medicalRecordRepository.save(medicalRecord);
    }

    public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord){
        return this.medicalRecordRepository.update(medicalRecord);
    }

    public  MedicalRecord deleteMedicalRecord(MedicalRecord medicalRecord){
        return this.medicalRecordRepository.delete(medicalRecord);
    }

    public String getBirthdayFromFirstnameAndLastname(String firstName, String lastName) {
        return this.medicalRecordRepository.getBirthdayFromFirstnameAndLastname(firstName, lastName);
    }

    public List<String> getMedicationFromFirstnameAndLastname(String firstName, String lastName) {
        return this.medicalRecordRepository.getMedicationFromFirstnameAndLastname(firstName, lastName);
    }

    public List<String> getAllergiesFromFirstnameAndLastname(String firstName, String lastName) {
        return this.medicalRecordRepository.getAllergiesFromFirstnameAndLastname(firstName, lastName);
    }
}
