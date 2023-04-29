package com.driver.services.impl;

import com.driver.model.PaymentMode;
import com.driver.model.Payment;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.model.Reservation;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {

        Reservation reservation=null;
        Integer bill=reservation.getSpot().getPricePerHour()*reservation.getNumberOfHours();
        if(amountSent < bill){
            throw new Exception("Insufficient Amount");
        }

        //If the mode contains a string other than "cash", "card", or "upi" (any character in uppercase or lowercase), throw "Payment mode not detected" exception.
        if (!(mode.equalsIgnoreCase("cash") || mode.equalsIgnoreCase("card") || mode.equalsIgnoreCase("upi"))){
            throw  new Exception("Payment mode not detected");
        }
        Payment payment = new Payment();
        if(mode.equals("cash"))
            payment.setPaymentMode(PaymentMode.CASH);

        else if(mode.equals("card"))
            payment.setPaymentMode(PaymentMode.CARD);

        else
            payment.setPaymentMode(PaymentMode.UPI);

        payment.setReservation(reservation);
        payment.setPaymentCompleted(true);
        reservation.setPayment(payment);
        reservationRepository2.save(reservation);

        return payment;

    }
}
