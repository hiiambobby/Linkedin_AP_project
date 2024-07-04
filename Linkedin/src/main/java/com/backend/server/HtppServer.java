package com.backend.server;


import com.backend.server.HTTPHandler.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;

import java.sql.SQLException;

public class HtppServer {
    public static void main(String[] args) {
        try {

            //Files.createDirectories(Paths.get("src/main/java/com/backend/server"));
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

            server.createContext("/login", new userActionsHandler());
            server.createContext("/signup", new userActionsHandler());
            server.createContext("/user", new userActionsHandler());
            server.createContext("/contactInfo", new ContactInfoHandler());
            server.createContext("/primaryInfo", new PrimaryInfoHandler());
            server.createContext("/education", new EducationHandler());
            server.createContext("/connect", new ConnectHandler());
            server.createContext("/follow", new FollowHandler());
            server.createContext("/message", new MessageHandler());



            server.start();

            System.out.println("Hi i am hearing u on port 8000 =) ...");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}