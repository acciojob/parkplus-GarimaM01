package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingspace =new ParkingLot();

        parkingLotRepository1.save(parkingspace);
        return parkingspace;
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        ParkingLot parkingspace=parkingLotRepository1.findById(parkingLotId).get();

        Spot spotspace=new Spot();
        spotspace.setPricePerHour(pricePerHour);
        spotspace.setParkingLot(parkingspace);

        if(numberOfWheels <= 2){
            spotspace.setSpotType(SpotType.TWO_WHEELER);
        }
        else if(numberOfWheels <= 4) {
            spotspace.setSpotType(SpotType.FOUR_WHEELER);
        }
        else if(numberOfWheels > 4) {
            spotspace.setSpotType(SpotType.OTHERS);
        }

        spotspace.setOccupied(false);
        parkingspace.getSpotList().add(spotspace);

        parkingLotRepository1.save(parkingspace);

        return spotspace;
    }

    @Override
    public void deleteSpot(int spotId) {
        Spot spotspace=spotRepository1.findById(spotId).get();
        ParkingLot parkingLot=spotspace.getParkingLot();

        parkingLot.getSpotList().remove(spotspace);
        spotRepository1.delete(spotspace);

    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        ParkingLot parkingspace=parkingLotRepository1.findById(parkingLotId).get();

        Spot spotspace=null;

        for(Spot ispace:parkingspace.getSpotList()){
            if (ispace.getId() == spotId){
                spotspace = ispace;
            }
        }
        spotspace.setParkingLot(parkingspace);
        spotspace.setPricePerHour(pricePerHour);

        spotRepository1.save(spotspace);
        return spotspace;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        parkingLotRepository1.deleteById(parkingLotId);
    }
}
