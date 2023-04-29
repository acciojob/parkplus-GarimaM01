package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        User customer;
        ParkingLot parkingspace;
        try {
            customer=userRepository3.findById(userId).get();
            parkingspace=parkingLotRepository3.findById(parkingLotId).get();
        }catch (Exception e){
            throw new Exception("Cannot make reservation");
        }
        Spot occupiedSpot=null;
        int price=Integer.MAX_VALUE;

        for(Spot ispace: parkingspace.getSpotList()){
            int wheels=0;
            if(ispace.getSpotType() == SpotType.TWO_WHEELER)
                wheels=2;
            if(ispace.getSpotType() == SpotType.FOUR_WHEELER)
                wheels=4;
            if(ispace.getSpotType() == SpotType.OTHERS)
                wheels=Integer.MAX_VALUE;

            if(!ispace.getOccupied() && numberOfWheels <= wheels && ispace.getPricePerHour()*timeInHours < price) {
                price=ispace.getPricePerHour()*timeInHours;
                occupiedSpot=ispace;
            }
        }
        if(occupiedSpot == null)
            throw new Exception("Cannot make reservation");

        Reservation reservation=new Reservation();
        reservation.setNumberOfHours(timeInHours);
        reservation.setUser(customer);
        reservation.setSpot(occupiedSpot);

        customer.getReservationList().add(reservation);
        occupiedSpot.getReservationList().add(reservation);

        occupiedSpot.setOccupied(true);

        //reservationRepository3.save(reservation);
        spotRepository3.save(occupiedSpot);
        userRepository3.save(customer);

        return  reservation;
    }
}
