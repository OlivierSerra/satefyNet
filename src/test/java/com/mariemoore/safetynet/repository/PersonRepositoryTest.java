package com.mariemoore.safetynet.repository;

import com.mariemoore.safetynet.model.Person;
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
@WebMvcTest(PersonRepository.class)
public class PersonRepositoryTest {
    @Autowired
    PersonRepository personRepository;
    @MockBean
    JsonDataGetter dataGetter;
    @MockBean
    private Validation validation;
    private List<Person> personList = new ArrayList<>();
    private Person john;
    private Person jacob;
    private Person tenley;

    @BeforeAll
    void setUp() throws Exception {
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
        personRepository = new PersonRepository(dataGetter, validation);
    }

    @Test
    public void findAllSuccessTest (){
        personRepository.persons = personList;
        List<Person> personResult = personRepository.findAll();
        Assertions.assertEquals(3, personResult.size());
    }
    @Test
    public void savePersonShouldReturnOk() {
        Person toSave = new Person(
                "Marie",
                "Moore",
                "1509 Main Road",
                "Culver",
                "97451",
                "841-874-6512",
                "MARIEMOORE@email.com");
        personRepository.persons = new ArrayList<>(dataGetter.getPersonsData());
        personRepository.save(toSave);
        Person personResult = personRepository.findPersonByFirstnameAndLastname("Marie", "Moore");
        Assertions.assertEquals("Marie", personResult.getFirstName());
        assertNotNull(personResult);
    }

    @Test
    public void updatePersonShouldReturnOk() {
        Person toSave = new Person(
                "Marie",
                "Moore",
                "1509 Main Road",
                "Culver",
                "97451",
                "841-874-6512",
                "MARIEMOORE@email.com");
        personRepository.persons = new ArrayList<>(dataGetter.getPersonsData());
        personRepository.save(toSave);
        Person marieExists = personRepository.findPersonByFirstnameAndLastname("Marie", "Moore");
        Assertions.assertEquals("Marie", marieExists.getFirstName());
        personRepository.update(new Person(
                "Marie",
                "Moore",
                "UPDATED ADDRESS",
                "UPDATED CITY",
                "97451",
                "000-000-000",
                "MARIEMOORE@email.com"
        ));
        Person marieUpdated = personRepository.findPersonByFirstnameAndLastname("Marie", "Moore");
        Assertions.assertEquals("UPDATED ADDRESS", marieUpdated.getAddress());
    }

    @Test
    public void deletePersonShouldReturnOk() {
        personRepository.save(new Person(
                "Marie",
                "Moore",
                "1509 Main Road",
                "Culver",
                "97451",
                "841-874-6512",
                "MARIEMOORE@email.com"));
        Person marieExists = personRepository.findPersonByFirstnameAndLastname("Marie", "Moore");
        Assertions.assertEquals("Marie", marieExists.getFirstName());
        personRepository.deletePerson("Marie", "Moore");
        Person marieDeleted = personRepository.findPersonByFirstnameAndLastname("Marie", "Moore");
        Assertions.assertEquals(null, marieDeleted);
    }

    @Test
    public void getHouseholdOfChildShouldReturnOk(){
        personRepository.persons = personList;
        List<Person> household = personRepository.getHouseholdOfChild(tenley.getFirstName(), tenley.getLastName());
        Assertions.assertEquals(2, household.size());
        Assertions.assertEquals("John", household.get(0).getFirstName());
        Assertions.assertEquals("Jacob", household.get(1).getFirstName());

    }

    @Test
    public void findPersonsByAddressShouldReturnOk(){
        personRepository.persons = personList;
        List<Person> peopleLivingAtAddressList = personRepository.findPersonsByAddress(john.getAddress());
        Assertions.assertEquals(3, peopleLivingAtAddressList.size());
    }
}
