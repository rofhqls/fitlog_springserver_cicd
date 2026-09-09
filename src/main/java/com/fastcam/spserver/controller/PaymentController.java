package com.fastcam.spserver.controller;

import com.fastcam.spserver.dto.PaymentConfirmRequest;
import com.fastcam.spserver.dto.PaymentConfirmResponse;
import com.fastcam.spserver.entity.Member;
import com.fastcam.spserver.entity.Payment;
import com.fastcam.spserver.entity.Subscription;
import com.fastcam.spserver.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/charge")
public class PaymentController {
    @Autowired
    PaymentService ps;

    @PostMapping("/confirm")
    public HashMap<String, Object> confirmPayment(@RequestBody PaymentConfirmRequest request, @RequestParam("mnum") int mnum) {
        HashMap<String, Object> map = new HashMap<>();

        PaymentConfirmResponse result = ps.confirmPayment(request, mnum);
        if(result.isDone()) {
            map.put("msg", "ok");
        } else {
            map.put("msg", result.status());
        }
        return map;
    }

    @GetMapping("/getPayment")
    public HashMap<String, Object> getPayment(@RequestParam("mnum") int mnum) {
        HashMap<String, Object> map = new HashMap<>();
        List<Payment> paymentList = ps.getPayment(mnum);
        map.put("paymentList", paymentList);
        return map;
    }

    @GetMapping("/getSubscription")
    public HashMap<String, Object> getSubscription(@RequestParam("mnum") int mnum) {
        HashMap<String, Object> map = new HashMap<>();

//        Payment payment = ps.getLatestPayment(mnum);
//
//        if(payment == null) {
//            map.put("subEnd", null);
//            return map;
//        }
//
//        java.time.LocalDate subEnd = payment.getIndate()
//                .toInstant()
//                .atZone(java.time.ZoneId.systemDefault())
//                .toLocalDate()
//                .plusDays(30);
//
//        map.put("subEnd", subEnd.toString());

        Subscription s = ps.getSubEnd(mnum);
        if(s == null) {
            map.put("subEnd", null);
        } else {
            map.put("subEnd",s.getSubEnd());
        }

        return map;
    }


}
