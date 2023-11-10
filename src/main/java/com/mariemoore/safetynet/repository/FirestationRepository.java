package com.mariemoore.safetynet.repository;

import com.mariemoore.safetynet.model.Firestation;
import com.mariemoore.safetynet.utils.Validation;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class FirestationRepository {
    public List<Firestation> firestations;
    private JsonDataGetter jsonDataGetter;
    private Validation validation;

    public FirestationRepository(JsonDataGetter jsonDataGetter, Validation validation){
        this.jsonDataGetter = jsonDataGetter;
        this.firestations = this.jsonDataGetter.getFirestationsData();
        this.validation = validation;
    }

    public List<Firestation> getFirestations() {
        return firestations;
    }

    public Firestation saveFirestation(Integer stationId, String stationAddress){
        Firestation f = new Firestation(stationId,stationAddress);
        if(this.validation.isFireStationInvalid(f)){
            return null;
        }
        this.firestations.add(f);
        return  f;
    }

    public Firestation updateFirestation(Firestation toUpdate){
        boolean isFoundAndValid = false;
        for(Firestation f: this.firestations){
            if(f.getAddress().equals(toUpdate.getAddress()) && !this.validation.isFireStationInvalid(toUpdate)){
                f.setStation(toUpdate.getStation());
                isFoundAndValid = true;
            }
        }
        return isFoundAndValid ? toUpdate : null;
    }

    public Firestation deleteFirestation(Firestation toDelete){
        Firestation deleted = null;
        List<Firestation> toKeep = new ArrayList<>();
        for(Firestation f: this.firestations){
            if(!f.getAddress().equals(toDelete.getAddress())){
                toKeep.add(f);
            }
            else if(f.getAddress().equals(toDelete.getAddress())){
                deleted = f;
            }
        }
        this.firestations = toKeep;
        return deleted;
    }

    public List<String> findAddressesByFirestation(Integer stationId){
        List<String> addresses = this.firestations.stream()
                .filter(firestation -> firestation.getStation().equals(stationId))
                .map(firestation -> firestation.getAddress())
                .collect(Collectors.toList());
        return addresses;
    }

    public Integer findStationNumberByAddress(String address){
        return this.firestations.stream()
                .filter(firestation -> Objects.equals(firestation.getAddress(), address))
                .map(firestation -> firestation.getStation())
                .findAny()
                .orElse(null);
    }
}
