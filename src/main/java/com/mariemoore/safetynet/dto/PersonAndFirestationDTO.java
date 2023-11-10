package com.mariemoore.safetynet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
public class PersonAndFirestationDTO {
    private List<PersonPhoneDTO> persons;
    private HashMap<String, Integer> nbOfAdultsAndChildren;
}
