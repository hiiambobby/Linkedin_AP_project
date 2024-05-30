package com.backend.LinkedinServer;

//import com.backend.LinkedinServer.HTTPHandler.UserHandler;
import com.backend.LinkedinServer.HTTPHandler.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import com.backend.LinkedinServer.HTTPHandler.UserHandler;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

public class HtppServer {
    public static void main(String[] args) {
        try {
            HttpServer server = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(8000), 0);


            server.createContext("/user", new UserHandler());
            server.createContext("/contactInfo", new ContactInfoHandler());


            server.start();

            System.out.println("Hi i am hearing u on port 80000 =) ...");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}