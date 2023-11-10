package com.mariemoore.safetynet.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mariemoore.safetynet.model.Content;
import com.mariemoore.safetynet.model.Firestation;
import com.mariemoore.safetynet.model.MedicalRecord;
import com.mariemoore.safetynet.model.Person;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Component
public class JsonDataGetter{
    List<Person> persons = new ArrayList<>();
    List<Firestation> firestations = new ArrayList<>();

    List<MedicalRecord> medicalRecords = new ArrayList<>();
    String filePath = "src/main/resources/data.json";

    Content content;

    public JsonDataGetter(){
        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = new File(this.filePath);
        try{
            content = objectMapper.readValue(jsonFile, Content.class);
            this.persons = this.content.getPersons();
            this.firestations = this.content.getFirestations();
            this.medicalRecords = this.content.getMedicalrecords();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Person> getPersonsData(){
        return this.persons;
    }

    public List<Firestation> getFirestationsData() {
        return this.firestations;
    }

    public List<MedicalRecord> getMedicalRecordsData() {
        return this.medicalRecords;
    }
}
