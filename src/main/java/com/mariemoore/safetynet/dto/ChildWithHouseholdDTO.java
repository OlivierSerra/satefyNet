package com.mariemoore.safetynet.dto;

import com.mariemoore.safetynet.model.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class ChildWithHouseholdDTO {
    private String firstname;
    private String lastname;
    private long age;
    private List<Person> household;
}
