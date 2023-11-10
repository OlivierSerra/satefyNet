package com.mariemoore.safetynet.controller;

import com.mariemoore.safetynet.jsonUtils;
import com.mariemoore.safetynet.model.Firestation;
import com.mariemoore.safetynet.service.FirestationService;
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
@WebMvcTest(controllers = FirestationController.class)
public class FirestationControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    FirestationService firestationService;

    private List<Firestation> firestationList;

    private Firestation firstFirestation;
    private Firestation secondFirestation;
    private Firestation thirdFirestation;

    @BeforeAll
    void setup() {
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
    public void getAllFirestationShouldReturnListOfFirestations() throws Exception{
        when(firestationService.getFirestations()).thenReturn(firestationList);
        mvc.perform(get("/firestation/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].station", is(3)))
                .andExpect(jsonPath("$[1].station", is(2)))
                .andExpect(jsonPath("$[2].station", is(3)));
    }

    @Test
    public void addFirestationShouldReturnFirestation() throws Exception{
        when(firestationService.addFirestation(firstFirestation.getStation(), firstFirestation.getAddress())).thenReturn(firstFirestation);
        mvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUtils.asJsonString(firstFirestation)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void addInvalidFirestationShouldNotReturnFirestation() throws Exception{
        when(firestationService.addFirestation(firstFirestation.getStation(), firstFirestation.getAddress())).thenReturn(null);
        mvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUtils.asJsonString(firstFirestation)))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateFirestationShouldReturnFirestation() throws Exception{
        when(firestationService.updateFirestation(firstFirestation)).thenReturn(firstFirestation);
        mvc.perform(put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUtils.asJsonString(firstFirestation)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void updateInvalidFirestationShouldNotReturnFirestation() throws Exception{
        when(firestationService.updateFirestation(firstFirestation)).thenReturn(null);
        mvc.perform(put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUtils.asJsonString(firstFirestation)))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteFirestationShouldReturnNoContent() throws Exception{
        when(firestationService.deleteFirestation(firstFirestation)).thenReturn(firstFirestation);
        mvc.perform(delete("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUtils.asJsonString(firstFirestation)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void deleteInvalidFirestationShouldReturnNoContent() throws Exception{
        when(firestationService.deleteFirestation(firstFirestation)).thenReturn(null);
        mvc.perform(delete("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUtils.asJsonString(firstFirestation)))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
