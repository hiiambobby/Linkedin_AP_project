package com.backend.LinkedinServer.HTTPHandler;

import com.backend.LinkedinServer.Model.ContactInfo;
import com.backend.LinkedinServer.Model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.backend.LinkedinServer.HTTPHandler.HttpStatusCode.*;

public class ContactInfoHandler implements HttpHandler{

    public static final ObjectMapper objectMapper = new ObjectMapper();
    private List<ContactInfo> contactInfos = new ArrayList<>();

    static {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String response = "";

        switch (method.toUpperCase()) {
            case "GET":
                response = getContactInfo();
                break;
            case "POST":
                response = createContactInfo(exchange);
                break;
            case "PUT":
                response = updateContactInfo(exchange);
                break;
            case "DELETE":
                response = deleteContactInfo(exchange);
                break;
            default:
                exchange.sendResponseHeaders(METHOD_NOT_ALLOWED, -1); //
                return;
        }

        exchange.sendResponseHeaders(OK, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private String getContactInfo() throws IOException {
        return objectMapper.writeValueAsString(contactInfos);
    }

    private String createContactInfo(HttpExchange exchange) {
        try {
            ContactInfo contactInfo = objectMapper.readValue(exchange.getRequestBody(), ContactInfo.class);
            contactInfos.add(contactInfo);
            return "Contact Info created successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error creating contact info: " + e.getMessage();
        }
    }

    private String updateContactInfo(HttpExchange exchange) throws IOException {
        ContactInfo contactInfo = objectMapper.readValue(exchange.getRequestBody(), ContactInfo.class);
        for (int i = 0; i < contactInfos.size(); i++) {
            if (contactInfos.get(i).getPhoneNumber().equals(contactInfo.getPhoneNumber())) {
                contactInfos.set(i, contactInfo);
                return objectMapper.writeValueAsString(contactInfo);
            }
        }
        return "Contact info not found";
    }

    private String deleteContactInfo(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("phoneNumber=")) {
            String phoneNumber = query.split("=")[1];
            for (int i = 0; i < contactInfos.size(); i++) {
                if (contactInfos.get(i).getPhoneNumber().equals(phoneNumber)) {
                    ContactInfo removed = contactInfos.remove(i);
                    return objectMapper.writeValueAsString(removed);
                }
            }
            return "Contact info not found";
        } else {
            return "Invalid query parameter";
        }
    }
}
//error in posting the contact info also i still don't know how to relate the user with the info