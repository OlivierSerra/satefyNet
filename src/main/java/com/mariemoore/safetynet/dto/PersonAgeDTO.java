package com.mariemoore.safetynet.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PersonAgeDTO {
    private String firstName;
    private String lastName;
    private long age;
}
