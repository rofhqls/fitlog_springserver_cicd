package com.fastcam.spserver.service;

import com.fastcam.spserver.verification.VerificationCode;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class SMSService {

    private final DefaultMessageService messageService;

    @Value("${coolsms.sender-phone}")
    private String senderPhone;

    private final HashMap<String, VerificationCode> codeList = new HashMap<>();


    public void sendSMS(String toPhone) {
        toPhone = toPhone.replace("-", "");

        int number = (int)(Math.random() * (900000)) + 100000;

        Message message = new Message();
        message.setFrom(senderPhone);
        message.setTo(toPhone);
        message.setText("[FITLOG] 인증번호는 [ " + number + " ] 입니다.\n"
                + "5분간 유효하며, 타인에게 공유하지 마세요.");
        SingleMessageSentResponse response =
                this.messageService.sendOne(new SingleMessageSendingRequest(message));

        codeList.put(toPhone,new VerificationCode(number));
    }

    public String confirmSMSCode(String userNumber, String phone) {
        phone = phone.replace("-", "");
        String msg = "";
        if(!codeList.containsKey(phone))
            msg = "먼저 인증번호를 발급받으세요.";
        else {
            VerificationCode vc = codeList.get(phone);
            if (vc.isExpired()) {
                msg = "만료된 인증번호입니다. 인증번호를 새로 발급받으세요.";
                codeList.remove(phone);
            }
            else {
                String code = String.valueOf(vc.getCode());
                if (code.equals(userNumber)) {
                    msg = "ok";
                    codeList.remove(phone);
                } else
                    msg = "잘못된 인증번호입니다.";
            }
        }
        return msg;
    }
}
