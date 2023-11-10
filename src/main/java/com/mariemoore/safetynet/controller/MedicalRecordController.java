package com.mariemoore.safetynet.controller;

import com.mariemoore.safetynet.model.MedicalRecord;
import com.mariemoore.safetynet.service.MedicalRecordService;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Objects;
import static org.apache.logging.log4j.LogManager.getLogger;

@RestController
@RequestMapping("/medicalrecord")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;
    private static final Logger logger = getLogger(MedicalRecordController.class);

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @GetMapping("/all")
    public List<MedicalRecord> getMedicalRecords(){
        logger.info("getting all medical records");
        return medicalRecordService.getMedicalRecords();
    }

    @ResponseBody
    @PostMapping
    public ResponseEntity<MedicalRecord> addMedicalRecord(@RequestBody MedicalRecord medicalRecord){
        MedicalRecord addedMedicalRecord = medicalRecordService.addMedicalRecord(medicalRecord);
        if(Objects.isNull(addedMedicalRecord)){
            logger.error("could not add medical record");
            return ResponseEntity.noContent().build();
        }
        logger.info("medical record added successfully");
        return ResponseEntity.ok().body(addedMedicalRecord);
    }

    @ResponseBody
    @PutMapping
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@RequestBody MedicalRecord medicalRecord){
        MedicalRecord updatedMedicalRecord = medicalRecordService.updateMedicalRecord(medicalRecord);
        if(Objects.isNull(updatedMedicalRecord)){
            logger.error("could not modify medical record");
            return ResponseEntity.noContent().build();
        }
        logger.info("medical records updated successfully");
        return ResponseEntity.ok().body(updatedMedicalRecord);
    }

    @ResponseBody
    @DeleteMapping
    public ResponseEntity<MedicalRecord> deleteMedicalRecord(@RequestBody MedicalRecord medicalRecord){
        MedicalRecord deletedMedicalRecord = medicalRecordService.deleteMedicalRecord(medicalRecord);
        if(Objects.isNull(deletedMedicalRecord)){
            logger.error("could not delete medical records");
        }
        logger.info("medical record added successfully");
        return ResponseEntity.noContent().build();
    }
}
