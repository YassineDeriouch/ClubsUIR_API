package com.example.api.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmailsService {
    @Autowired
    private JavaMailSender javaMailSender;
    public void SendEmail(String toEmail,String body,String subject) throws MessagingException {
        MimeMessage mimeMessage=javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage,true);
        mimeMessageHelper.setTo(toEmail);
        mimeMessageHelper.setFrom("youssef.bouichenade@gmail.com");
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(body);
        javaMailSender.send(mimeMessage);
    }

    public void sendMeetingCreatedEmailToMembers(List<String> memberEmails, String meetingName) throws MessagingException {
        String subject = "Nouvelle réunion : " + meetingName;
        String body = "Chers membres de la réunion,\nUne nouvelle réunion nommée " + meetingName + " a été créée.Soyer au rendez-vous.\nCordialement.";

        for (String memberEmail : memberEmails) {
            SendEmail(memberEmail,body,subject );
        }
    }


}