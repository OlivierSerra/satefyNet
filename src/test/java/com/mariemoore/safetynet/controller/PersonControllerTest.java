package com.mariemoore.safetynet.controller;

import com.mariemoore.safetynet.jsonUtils;
import com.mariemoore.safetynet.model.Person;
import com.mariemoore.safetynet.service.PersonService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(controllers = PersonController.class)
public class PersonControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    PersonService personService;

    private List<Person> personList;
    private Person john;
    private Person jacob;
    private Person tenley;

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
    }

    @Test
    public void getAllPersonsShouldReturnListOfPersons() throws Exception{
        when(personService.getPersons()).thenReturn(personList);
        mvc.perform(get("/person/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[1].firstName", is("Jacob")))
                .andExpect(jsonPath("$[2].firstName", is("Tenley")));
    }

    @Test
    public void addPersonShouldReturnPerson() throws Exception {
        when(personService.addPerson(john)).thenReturn(john);
        mvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUtils.asJsonString(john)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void addPersonWhoAlreadyExistsShouldNotReturnPerson() throws Exception {
        when(personService.addPerson(john)).thenReturn(null);
        mvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUtils.asJsonString(john)))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void updatePersonShouldReturnPerson() throws Exception {
        when(personService.updatePerson(john)).thenReturn(john);
        mvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUtils.asJsonString(john)))
                .andDo(print())
                .andExpect(result -> assertEquals(jsonUtils.asJsonString(john), result.getResponse().getContentAsString()))
                .andExpect(status().isOk());
    }

    @Test
    public void updatePersonWhoDoesNotExistShouldNotReturnPerson() throws Exception {
        when(personService.updatePerson(john)).thenReturn(null);
        mvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUtils.asJsonString(john)))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void deletePersonShouldReturnNoContent() throws Exception {
        when(personService.deletePerson(john.getFirstName(), john.getLastName())).thenReturn(john);
        mvc.perform(delete("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUtils.asJsonString(john)))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void deletePersonWhoDoesNotExistsShouldReturnNoContent() throws Exception {
        when(personService.deletePerson(john.getFirstName(), john.getLastName())).thenReturn(null);
        mvc.perform(delete("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUtils.asJsonString(john)))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

}
