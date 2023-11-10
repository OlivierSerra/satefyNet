package com.mariemoore.safetynet.controller;

import com.mariemoore.safetynet.model.Person;
import com.mariemoore.safetynet.service.PersonService;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Objects;
import static org.apache.logging.log4j.LogManager.getLogger;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonService personService;
    private static final Logger logger = getLogger(PersonController.class);

    public PersonController(PersonService personService){
        this.personService = personService;
    }

    @GetMapping("/all")
    public List<Person> getPersons(){
        logger.info("getting all persons");
        return personService.getPersons();
    }

    @ResponseBody
    @PostMapping
    public ResponseEntity<Person> addPerson(@RequestBody Person person){
        Person addedPerson = personService.addPerson(person);
        if(Objects.isNull(addedPerson)){
            logger.error("could not add person");
            return ResponseEntity.noContent().build();
        }
        logger.info("person added successfully");
        return ResponseEntity.ok().body(addedPerson);
    }

    @ResponseBody
    @PutMapping
    public ResponseEntity<Person> updatePerson(@RequestBody Person person){
        Person updatedPerson = personService.updatePerson(person);
        if(Objects.isNull(updatedPerson)){
            logger.error("could not modify person");
            return ResponseEntity.noContent().build();
        }
        logger.info("person updated successfully");
        return ResponseEntity.ok().body(updatedPerson);
    }

    @ResponseBody
    @DeleteMapping
    public ResponseEntity<Person> deletePerson(@RequestBody Person person){
        Person deletedPerson = personService.deletePerson(person.getFirstName(), person.getLastName());
        if(Objects.isNull(deletedPerson)){
            logger.error("could not delete person");
        }
        logger.info("person deleted successfully");
        return ResponseEntity.noContent().build();
    }
}
