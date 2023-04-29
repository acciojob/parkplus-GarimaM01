package com.driver.controllers;

import com.driver.services.impl.PaymentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/payment")
public class PaymentController {
	
	@Autowired
    PaymentServiceImpl paymentService;

    @PostMapping("/pay")
    public Payment pay(@RequestParam Integer reservationId, @RequestParam Integer amountSent, @RequestParam String mode) throws Exception{
        //Attempt a payment of amountSent for reservationId using the given mode ("cASh", "card", or "upi")
        Payment payment = paymentService.getPaymentByReservationId(reservationId);

        //If the amountSent is less than bill, throw "Insufficient Amount" exception, otherwise update payment attributes
        if(payment==null){
            throw new Exception("Payment not found for this Id: " + reservationId);
        }
        Integer bill =payment.getAmount();
        if(amountSent < bill){
            throw new Exception("Insufficient Amount");
        }

        //If the mode contains a string other than "cash", "card", or "upi" (any character in uppercase or lowercase), throw "Payment mode not detected" exception.
        if (!(mode.equalsIgnoreCase("cash") || mode.equalsIgnoreCase("card") || mode.equalsIgnoreCase("upi"))){
            throw  new Exception("Payment mode not detected");
        }


        //Note that the reservationId always exists
        payment.setAmountPaid(amountSent);
        payment.setMode(mode);
        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        paymentService.updatePayment(payment);
        return payment;
//        return  paymentService.pay(reservationId, amountSent, mode);
    }
}
