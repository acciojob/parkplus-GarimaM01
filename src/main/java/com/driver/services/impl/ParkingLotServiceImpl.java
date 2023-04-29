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
        ParkingLot parkingLot =new ParkingLot();
        parkingLot.setName(name);
        parkingLot.setAddress(address);

        parkingLotRepository1.save(parkingLot);
        return parkingLot;
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        ParkingLot parkingLot=parkingLotRepository1.findById(parkingLotId).get();

        Spot spotSpace=new Spot();
        spotSpace.setPricePerHour(pricePerHour);
        spotSpace.setOccupied(false);
        spotSpace.setParkingLot(parkingLot);

        if(numberOfWheels <= 2)
            spotSpace.setSpotType(SpotType.TWO_WHEELER);
        else if(numberOfWheels <= 4)
            spotSpace.setSpotType(SpotType.FOUR_WHEELER);
        else if(numberOfWheels > 4)
            spotSpace.setSpotType(SpotType.OTHERS);

        parkingLot.getSpotList().add(spotSpace);
        parkingLotRepository1.save(parkingLot);

        return spotSpace;
    }

    @Override
    public void deleteSpot(int spotId) {

        if(!spotRepository1.existsById(spotId)){
            return;
        }
        Spot spotSpace=spotRepository1.findById(spotId).get();
        ParkingLot parkingLot=spotSpace.getParkingLot();

        parkingLot.getSpotList().remove(spotSpace);
        spotRepository1.delete(spotSpace);

    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        ParkingLot parkingLot=parkingLotRepository1.findById(parkingLotId).get();

        Spot spotSpace=null;

        for(Spot spotss:parkingLot.getSpotList()){
            if(spotss.getId() == spotId){
                spotss.setPricePerHour(pricePerHour);
                spotSpace=spotss;
                spotRepository1.save(spotss);
                break;
            }
        }
        return spotSpace;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        parkingLotRepository1.deleteById(parkingLotId);

    }
}