package com.mariemoore.safetynet.model;

import lombok.*;

/**
 * Person class creates objects containing common properties
 * with Medical Record objects (firstname, lastname)
 * The age, medicines and allergies properties of a Person object
 * are therefore in the MedicalRecord object.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Person {

    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String zip;
    private String phone;
    private String email;

}
