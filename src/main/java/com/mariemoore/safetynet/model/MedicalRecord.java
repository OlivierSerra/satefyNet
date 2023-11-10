package com.mariemoore.safetynet.model;

import lombok.*;
import java.util.List;

/**
 * Medical Record class creates objects containing common properties
 * with Person objects (firstname, lastname)
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MedicalRecord {
    private String firstName;
    private String lastName;
    private String birthdate;
    private List<String> medications;
    private List<String> allergies;

}
