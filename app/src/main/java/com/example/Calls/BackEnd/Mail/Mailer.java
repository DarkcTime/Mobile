package com.example.Calls.BackEnd.Mail;

import com.example.Calls.BackEnd.Contacts.Contacts;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mailer {

    String to = "cerebrofon@yandex.ru";
    String from = "testsiteivans@gmail.com";
    String host = "smtp.gmail.com";
    String username = "testsiteivans@gmail.com";
    String password = "123qweASD!qweASD";
    String port = "587";

    public void SendMail(String code, Contacts contact, String body) {
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", port);
        properties.setProperty("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        session.setDebug(true);
        try {
            String num = contact.getPhoneNumberCurrentContact().replace("+","").
                    replace("-","").replace("(","")
                    .replace(")","").replace(" ","").substring(1);

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(code);

            String str = contact.getNameCurrentContact() + " <"+ num +"@personguide1023.ru>\n" + body;
            message.setText(str);
            message.setHeader("Content-Transfer-Encoding","8bit");

            Transport.send(message); System.out.println("Email Sent successfully....");
        } catch (MessagingException mex){ mex.printStackTrace(); }
    }
}
