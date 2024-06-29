package com.backend.LinkedinServer;


import com.backend.LinkedinServer.HTTPHandler.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

public class HtppServer {
    public static void main(String[] args) {
        try {

            //Files.createDirectories(Paths.get("src/main/java/com/backend/server"));
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

            server.createContext("/login", new userActionsHandler());
            server.createContext("/signup", new userActionsHandler());
            server.createContext("/user", new userActionsHandler());
          //  server.createContext("/user", new userActionsHandler());
            //server.createContext("/contactInfo", new ContactInfoHandler());

            server.start();

            System.out.println("Hi i am hearing u on port 80000 =) ...");
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