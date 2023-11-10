package com.mariemoore.safetynet.model;

import lombok.*;

/**
 * Firestation class creates objects containing a common propertie
 * with Person objects (address)
 * A Firestation can have multiple addresses
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Firestation {
    private Integer station;
    private String address;
}
