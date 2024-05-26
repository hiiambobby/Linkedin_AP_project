package com.backend.LinkedinServer.HTTPHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.backend.LinkedinServer.Model.ContactInfo;
import java.io.IOException;
import java.io.OutputStream;
import java.time.MonthDay;
import java.util.HashMap;
import java.util.Map;

public class ContactInfoHandler implements HttpHandler {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Map<String, ContactInfo> contactInfoMap = new HashMap<>();

    static {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String response = "";

        switch (method.toUpperCase()) {
            case "GET":
                response = getContactInfo(exchange);
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
                exchange.sendResponseHeaders(405, -1); // 405 Method Not Allowed
                return;
        }

        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private String getContactInfo(HttpExchange exchange) throws IOException {
        String response;
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("phoneNumber=")) {
            String phoneNumber = query.split("=")[1];
            ContactInfo contactInfo = contactInfoMap.get(phoneNumber);
            if (contactInfo != null) {
                response = objectMapper.writeValueAsString(contactInfo);
            } else {
                response = "Contact info not found";
            }
        } else {
            response = "Invalid query parameter";
        }
        return response;
    }

    private String createContactInfo(HttpExchange exchange) throws IOException {
        ContactInfo contactInfo = objectMapper.readValue(exchange.getRequestBody(), ContactInfo.class);
        contactInfoMap.put(contactInfo.getPhoneNumber(), contactInfo);
        return objectMapper.writeValueAsString(contactInfo);
    }

    private String updateContactInfo(HttpExchange exchange) throws IOException {
        ContactInfo contactInfo = objectMapper.readValue(exchange.getRequestBody(), ContactInfo.class);
        if (contactInfoMap.containsKey(contactInfo.getPhoneNumber())) {
            contactInfoMap.put(contactInfo.getPhoneNumber(), contactInfo);
            return objectMapper.writeValueAsString(contactInfo);
        } else {
            return "Contact info not found";
        }
    }

    private String deleteContactInfo(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("phoneNumber=")) {
            String phoneNumber = query.split("=")[1];
            ContactInfo removed = contactInfoMap.remove(phoneNumber);
            if (removed != null) {
                return "Deleted contact info for phone number: " + phoneNumber;
            } else {
                return "Contact info not found";
            }
        } else {
            return "Invalid query parameter";
        }
    }
}

//error in posting the contact info also i still don't know how to relate the user with the info