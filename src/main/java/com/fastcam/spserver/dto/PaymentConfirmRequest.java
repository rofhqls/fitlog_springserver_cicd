package com.fastcam.spserver.dto;

public record PaymentConfirmRequest(
        String paymentKey,
        String orderId,
        Long amount,
        String orderName
) {

}
