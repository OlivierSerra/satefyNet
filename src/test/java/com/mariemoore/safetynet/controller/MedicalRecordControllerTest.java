package com.mariemoore.safetynet.controller;

import com.mariemoore.safetynet.jsonUtils;
import com.mariemoore.safetynet.model.MedicalRecord;
import com.mariemoore.safetynet.service.MedicalRecordService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(controllers = MedicalRecordController.class)
public class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    MedicalRecordService medicalRecordService;

    private List<MedicalRecord> medicalRecordList;

    private MedicalRecord johnsMedicalRecord;
    private MedicalRecord jacobsMedicalRecord;
    private MedicalRecord tenleyMedicalRecord;

    @BeforeAll
    void setup() {
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
    public void getAllMedicalRecordsShouldReturnListOfMedicalRecords() throws Exception{
        when(medicalRecordService.getMedicalRecords()).thenReturn(medicalRecordList);
        mvc.perform(get("/medicalrecord/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[1].firstName", is("Jacob")))
                .andExpect(jsonPath("$[2].firstName", is("Tenley")));
    }

    @Test
    public void addMedicalRecordShouldReturnMedicalRecord() throws Exception{
        when(medicalRecordService.addMedicalRecord(johnsMedicalRecord)).thenReturn(johnsMedicalRecord);
        mvc.perform(post("/medicalrecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUtils.asJsonString(johnsMedicalRecord)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void addInvalidMedicalRecordShouldNotReturnMedicalRecord() throws Exception{
        when(medicalRecordService.addMedicalRecord(johnsMedicalRecord)).thenReturn(null);
        mvc.perform(post("/medicalrecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUtils.asJsonString(johnsMedicalRecord)))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateMedicalRecordShouldReturnMedicalRecord() throws Exception{
        when(medicalRecordService.updateMedicalRecord(johnsMedicalRecord)).thenReturn(johnsMedicalRecord);
        mvc.perform(put("/medicalrecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUtils.asJsonString(johnsMedicalRecord)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void updateMedicalRecordWithInvalidPropertiesShouldNotUpdateAndReturnMedicalRecord() throws Exception{
        when(medicalRecordService.updateMedicalRecord(johnsMedicalRecord)).thenReturn(null);
        mvc.perform(put("/medicalrecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUtils.asJsonString(johnsMedicalRecord)))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteMedicalRecordShouldShouldReturnNoContent() throws Exception{
        when(medicalRecordService.updateMedicalRecord(johnsMedicalRecord)).thenReturn(johnsMedicalRecord);
        mvc.perform(delete("/medicalrecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUtils.asJsonString(johnsMedicalRecord)))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteMedicalRecordWhichDoesNotExistsShouldReturnNoContent() throws Exception{
        when(medicalRecordService.updateMedicalRecord(johnsMedicalRecord)).thenReturn(null);
        mvc.perform(delete("/medicalrecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUtils.asJsonString(johnsMedicalRecord)))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
