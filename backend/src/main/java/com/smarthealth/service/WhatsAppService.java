package com.smarthealth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.*;
import java.net.http.*;

@Service
public class WhatsAppService {
    @Value("${twilio.account-sid:}") String sid;
    @Value("${twilio.auth-token:}") String token;
    @Value("${twilio.whatsapp-from:}") String from;

    public boolean configured(){return sid!=null&&!sid.isBlank()&&token!=null&&!token.isBlank()&&from!=null&&!from.isBlank();}
    public void sendText(String to,String body){
        // This project deliberately keeps Twilio optional. Configure credentials to enable WhatsApp.
        if(!configured()) return;
        try{
            String form="To="+URLEncoder.encode("whatsapp:"+to,java.nio.charset.StandardCharsets.UTF_8)
                    +"&From="+URLEncoder.encode(from,java.nio.charset.StandardCharsets.UTF_8)
                    +"&Body="+URLEncoder.encode(body,java.nio.charset.StandardCharsets.UTF_8);
            HttpRequest req=HttpRequest.newBuilder().uri(URI.create("https://api.twilio.com/2010-04-01/Accounts/"+sid+"/Messages.json"))
                .header("Content-Type","application/x-www-form-urlencoded")
                .header("Authorization","Basic "+java.util.Base64.getEncoder().encodeToString((sid+":"+token).getBytes()))
                .POST(HttpRequest.BodyPublishers.ofString(form)).build();
            HttpClient.newHttpClient().send(req,HttpResponse.BodyHandlers.ofString());
        }catch(Exception ignored){}
    }
}
