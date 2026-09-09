package com.fastcam.spserver.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//토스페이먼츠 응답에 이 필드들 외에 훨씬 많은 필드가 있지만.
//지금 필요한 것만 우선 매핑한다. (카드사, 영수증 URL 등은 필요할 때 추가)
//검증데이터결과(Json 데이터) -> Record
@JsonIgnoreProperties(ignoreUnknown = true)
public record PaymentConfirmResponse(
        String paymentKey,
        String orderId,
        String status, //Done, Waiting_for_deposit, Canceled, Partial_Canceled, expired, Aborted 등
        Long totalAmount
) {
    //record변수를 이용해서 isDone()을 호출하면 status가 Done일 때, true, 아닐 때 false
    public boolean isDone() {
        return "DONE".equals(status);
    }
}
