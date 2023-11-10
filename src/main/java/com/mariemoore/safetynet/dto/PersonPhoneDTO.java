package com.mariemoore.safetynet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PersonPhoneDTO {
    private String firstName;
    private String lastName;
    private String phone;
}
