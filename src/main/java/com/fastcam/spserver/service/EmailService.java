package com.fastcam.spserver.service;

import com.fastcam.spserver.repository.MemberRepository;
import com.fastcam.spserver.verification.VerificationCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender JMSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    private final HashMap<String, VerificationCode> codeList = new HashMap<>();

    public String sendEmail(String email) {
        String msg = "";
        int number = (int) (Math.random() * (900000)) + 100000;
        MimeMessage message = JMSender.createMimeMessage();
        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("[FITLOG] 이메일 인증번호 안내");
            String body = "" + "<!DOCTYPE html>" + "<html lang='ko'>" + "<head><meta charset='UTF-8'></head>" + "<body style='margin:0; padding:0; background-color:#f4f4f4; font-family: Arial, sans-serif;'>" + "  <table width='100%' cellpadding='0' cellspacing='0' style='background-color:#f4f4f4; padding: 40px 0;'>" + "    <tr><td align='center'>" + "      <table width='500' cellpadding='0' cellspacing='0' style='background-color:#ffffff; border-radius:8px; overflow:hidden; box-shadow: 0 2px 8px rgba(0,0,0,0.08);'>"
                    // 헤더
                    + "        <tr>" + "          <td style='background-color:#20D793; padding: 32px; text-align:center;'>" + "            <h1 style='margin:0; color:#ffffff; font-size:24px; letter-spacing:2px;'>FITLOG</h1>" + "          </td>" + "        </tr>"
                    // 본문
                    + "        <tr>" + "          <td style='padding: 40px 48px; text-align:center;'>" + "            <p style='margin:0 0 8px 0; font-size:16px; color:#374151;'>이메일 인증 요청이 들어왔습니다.</p>" + "            <p style='margin:0 0 32px 0; font-size:14px; color:#9CA3AF;'>아래 인증번호를 입력창에 입력해 주세요.</p>"
                    // 인증번호 박스
                    + "            <div style='display:inline-block; background-color:#F3F4F6; border-radius:8px; padding: 20px 48px; margin-bottom:32px;'>" + "              <span style='font-size:36px; font-weight:bold; letter-spacing:8px; color:#20D793;'>" + number + "</span>" + "            </div>" + "            <p style='margin:0 0 8px 0; font-size:13px; color:#6B7280;'>인증번호는 <strong>5분간</strong> 유효합니다.</p>" + "            <p style='margin:0; font-size:13px; color:#6B7280;'>본인이 요청하지 않은 경우 이 메일을 무시하세요.</p>" + "          </td>" + "        </tr>"
                    // 푸터
                    + "        <tr>" + "          <td style='background-color:#F9FAFB; padding: 20px 48px; text-align:center; border-top: 1px solid #E5E7EB;'>" + "            <p style='margin:0; font-size:12px; color:#9CA3AF;'>© 2026 FITLOG. All rights reserved.</p>" + "            <p style='margin:4px 0 0 0; font-size:12px; color:#9CA3AF;'>본 메일은 발신 전용입니다. 회신하지 마세요.</p>" + "          </td>" + "        </tr>" + "      </table>" + "    </td></tr>" + "  </table>" + "</body>" + "</html>";
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        JMSender.send(message);
        codeList.put(email, new VerificationCode(number));
        msg = "ok";
        return msg;
    }


    public String confirmEmailCode(String userNumber, String email) {
        String msg = "";
        if (!codeList.containsKey(email)) msg = "먼저 인증번호를 발급받으세요.";
        else {
            VerificationCode vc = codeList.get(email);
            if (vc.isExpired()) {
                msg = "만료된 인증번호입니다. 인증번호를 새로 발급받으세요.";
                codeList.remove(email);
            } else {
                String code = String.valueOf(vc.getCode());
                if (code.equals(userNumber)) {
                    msg = "ok";
                    codeList.remove(email);
                } else msg = "잘못된 인증번호입니다.";
            }
        }
        return msg;
    }
}
