package com.fastcam.spserver.service;

import com.fastcam.spserver.dto.PaymentConfirmRequest;
import com.fastcam.spserver.dto.PaymentConfirmResponse;
import com.fastcam.spserver.entity.Member;
import com.fastcam.spserver.entity.Payment;
import com.fastcam.spserver.entity.Subscription;
import com.fastcam.spserver.repository.MemberRepository;
import com.fastcam.spserver.repository.PaymentRepository;
import com.fastcam.spserver.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService {
    @Value("${toss.secret-key}")
    private String secretKey;

    private final RestClient restClient = RestClient.create("https://api.tosspayments.com");

    @Autowired
    PaymentRepository pr;

    @Autowired
    SubscriptionRepository sr;

    @Autowired
    MemberRepository mr;

    public PaymentConfirmResponse confirmPayment(PaymentConfirmRequest request, int mnum) {
        Member member = mr.findByNum(mnum);
        String authorization = "Basic " + Base64.getEncoder()
                .encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));

        try {
            PaymentConfirmResponse result = restClient.post()
                    .uri("/v1/payments/confirm")
                    .header(HttpHeaders.AUTHORIZATION, authorization)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                            "paymentKey", request.paymentKey(),
                            "orderId", request.orderId(),
                            "amount", request.amount()
                    ))
                    .retrieve()
                    .body(PaymentConfirmResponse.class);

            //결제 승인 성공
            if (result.isDone()) {

                System.out.println("========== 결제 확인 ==========");
                System.out.println("paymentKey : " + request.paymentKey());
                System.out.println("orderId    : " + request.orderId());
                System.out.println("amount     : " + request.amount());
                System.out.println("orderName  : " + request.orderName());
                System.out.println("==============================");

                Payment payment = new Payment();
                payment.setMember(member);
                payment.setPaymentKey(result.paymentKey());
                payment.setPrice(result.totalAmount());
                payment.setProductName(request.orderName());

                pr.save(payment);

                int days = getSubscriptionDays(request.orderName());

                Subscription sub = sr.findByMember(member).orElse(null);
                LocalDate today = LocalDate.now();

                //구독 정보 처리
                if(sub == null) {
                    //결제 처음하는 경우
                    sub = new Subscription();
                    sub.setMember(member);
                    sub.setSubStart(today);
                    sub.setSubEnd(today.plusDays(days));
                    sr.save(sub);
                } else if (sub.getSubEnd().isAfter(today)) {
                    //기존 구독이 남아있는 경우
                    sub.setSubEnd(sub.getSubEnd().plusDays(days));
                    sr.save(sub);
                } else {
                    //기존 구독이 만료된 경우
//                    sub.setSubStart(today);
//                    sub.setSubEnd(today.plusDays(days));
                    Subscription newSub = new Subscription();
                    newSub.setMember(member);
                    newSub.setSubStart(today);
                    newSub.setSubEnd(today.plusDays(days));
                    sr.save(newSub);
                }

//                sr.save(sub);
            }
            return result;

        } catch (HttpClientErrorException e) {
            // 토스페이먼츠가 4xx로 내려주는 에러 바디(코드/메시지)를 그대로 전달
            throw new RuntimeException("결제 승인 실패: " + e.getResponseBodyAsString(), e);
        }
    }

    private int getSubscriptionDays(String orderName) {
        return switch (orderName) {
            case "월 정기구독" -> 30;
            case "6개월 구독" -> 180;
            case "년 정기구독" -> 365;
            default -> throw new IllegalArgumentException("잘못된 구독 상품입니다.");
        };
    }

    public List<Payment> getPayment(int mnum) {
        Member member = mr.findByNum(mnum);
        return pr.findByMemberOrderByIndateDesc(member);
    }

    public Payment getLatestPayment(int mnum) {
        List<Payment> list = pr.findByMemberNumOrderByIndateDesc(mnum);

        if(list.isEmpty()) return null;

        return list.get(0);
    }


    public Subscription getSubEnd(int mnum) {
        List<Subscription> list = sr.findByMemberNumOrderBySubEndDesc(mnum);
        if(list.isEmpty()) return null;
        return list.get(0);
    }
}