package com.backend.LinkedinServer.HTTPHandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.backend.LinkedinServer.Model.ContactInfo;
import java.io.IOException;
import java.io.OutputStream;
import java.time.MonthDay;

public class ContactInfoHandler implements HttpHandler {

    private static final ObjectMapper objectMapper = new ObjectMapper();

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
                response = deleteContactInfo();
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

    private String getContactInfo() {
        ContactInfo contactInfo = new ContactInfo(
                "http://example.com/profile",
                "1234567890",
                "Mobile",
                MonthDay.of(1, 1),
                "123 Main St",
                "example@example.com"
        );
        try {
            return objectMapper.writeValueAsString(contactInfo);
        } catch (IOException e) {
            e.printStackTrace();
            return "Error getting contact info";
        }
    }

    private String createContactInfo(HttpExchange exchange) throws IOException {
        ContactInfo contactInfo = objectMapper.readValue(exchange.getRequestBody(), ContactInfo.class);
        return "Created contact info for " + contactInfo.getPhoneNumber();
    }

    private String updateContactInfo(HttpExchange exchange) throws IOException {
        ContactInfo contactInfo = objectMapper.readValue(exchange.getRequestBody(), ContactInfo.class);
        return "Updated contact info for " + contactInfo.getPhoneNumber();
    }

    private String deleteContactInfo() {
        return "Deleted contact info";
    }
}


//error in posting the contact info also i still don't know how to relate the user with the info