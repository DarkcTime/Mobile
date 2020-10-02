package com.example.Calls.BackEnd.Mail;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.Calls.BackEnd.Contacts.Contacts;
import com.example.Calls.MainActivity;

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

    final String to = "vlad_chekmarev_01@mail.ru";
    final String from = "sgk.inventories@gmail.com";
    final String host = "smtp.gmail.com";
    final String username = "sgk.inventories@gmail.com";
    final String password = "wlad620!wlad620!";
    final String port = "587";

    public  Mailer(){

    }

    public void SendMessageAboutError(Exception ex, MainActivity mainActivity){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{to});
        i.putExtra(Intent.EXTRA_SUBJECT, "Error from Android Application");
        i.putExtra(Intent.EXTRA_TEXT   , ex.getMessage());
        try {
            mainActivity.startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException e) {
            Toast.makeText(mainActivity, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private MainActivity mainActivity;

    public void SendMain(String message, EmailType type){


    }


    public void SendMail2(String code, String body) {
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
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(code);
            String str = body;
            message.setText(str);
            message.setHeader("Content-Transfer-Encoding","8bit");

            Transport.send(message); System.out.println("Email Sent successfully....");


        } catch (Exception ex){
            Log.d("mailer", ex.getMessage());
        }
    }

    public enum EmailType{
        Error, Path
    }
}


