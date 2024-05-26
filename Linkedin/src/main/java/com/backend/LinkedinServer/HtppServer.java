package com.backend.LinkedinServer;

//import com.backend.LinkedinServer.HTTPHandler.UserHandler;
import com.backend.LinkedinServer.HTTPHandler.*;

import java.net.InetSocketAddress;
import com.backend.LinkedinServer.HTTPHandler.UserHandler;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

public class HtppServer {
    public static void main(String[] args) throws Exception {
        // Set up HTTP server on port 4567
        com.sun.net.httpserver.HttpServer server = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(4567), 0);

        // Create context for '/users' endpoint and set handler
        server.createContext("/users", new UserHandler());
        server.createContext("/contactInfo", new ContactInfoHandler());

        // Start the server
        server.start();

        System.out.println("Server is listening on port 4567");
    }
}