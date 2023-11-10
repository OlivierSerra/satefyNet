package com.mariemoore.safetynet.repository;

import com.mariemoore.safetynet.model.Person;
import com.mariemoore.safetynet.utils.Validation;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class PersonRepository{
    public List<Person> persons;
    private JsonDataGetter jsonDataGetter;
    private Validation validation;

    public PersonRepository(JsonDataGetter jsonDataGetter, Validation validation) throws Exception {
        this.jsonDataGetter = jsonDataGetter;
        this.persons = this.jsonDataGetter.getPersonsData();
        this.validation = validation;
    }

    public List<Person> findAll() {
        return this.persons;
    }

    public Person save(Person person) {
        Person alreadyExists = this.persons.stream().filter(p ->
            p.getFirstName().equals(person.getFirstName())  && p.getLastName().equals(person.getLastName())
        ).findAny().orElse(null);

        if(this.validation.isPersonInvalid(person) || Objects.nonNull(alreadyExists)){
            return null;
        }
        this.persons.add(person);
        return person;
    }

    public Person update(Person toUpdate){
        Boolean isFoundAndValid = false;
        for(Person p: this.persons){
            if(p.getFirstName().equals(toUpdate.getFirstName()) && p.getLastName().equals(toUpdate.getLastName()) && !this.validation.isPersonInvalid(toUpdate)){
                isFoundAndValid = true;
                p.setAddress(toUpdate.getAddress());
                p.setCity(toUpdate.getCity());
                p.setZip(toUpdate.getZip());
                p.setPhone(toUpdate.getPhone());
                p.setEmail(toUpdate.getEmail());
            }
        }
        return isFoundAndValid ? toUpdate : null;
    }

    public Person deletePerson(String firstname, String lastname) {
        Person deleted = null;
        List<Person> toKeep = new ArrayList<>();
        for(Person p : this.persons){
           if(!p.getFirstName().equals(firstname) && !p.getLastName().equals(lastname)){
               toKeep.add(p);
           }
           else if(p.getFirstName().equals(firstname) && p.getLastName().equals(lastname)){
               deleted = p;
           }
        }
        this.persons = toKeep;
        return deleted;
    }

    public Person findPersonByFirstnameAndLastname(String firstname, String lastname){
        return this.persons.stream()
                .filter(person ->
                        Objects.equals(person.getFirstName(), firstname) &&
                        Objects.equals(person.getLastName(), lastname))
                .findAny()
                .orElse(null);
    }

    public List<Person> getHouseholdOfChild(String firstname, String lastname) {
        Person child = findPersonByFirstnameAndLastname(firstname, lastname);
        List<Person> household = new ArrayList<>();
        for(Person p : this.persons){
            if(Objects.equals(p.getLastName(), child.getLastName()) &&
                    Objects.equals(p.getAddress(), child.getAddress()) &&
                    !Objects.equals(p.getFirstName(), child.getFirstName())
            ){
                household.add(p);
            }
        }
        return household;
    }

    public List<Person> findPersonsByAddress(String address){
        return this.persons.stream()
                .filter(person -> Objects.equals(person.getAddress(), address))
                .collect(Collectors.toList());
    }
}
