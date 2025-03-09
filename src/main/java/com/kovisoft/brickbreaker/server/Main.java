package com.kovisoft.brickbreaker.server;

import com.kovisoft.brickbreaker.exports.BBModulePage;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;

public class Main {

    /**
     * This is to allow more localized testing on localhost, not actually intended to run its own server, generally speaking.
     * @param args -port int is the only allowable argument for the resource retrieval, defaults to 8030, always runs on port 8020
     */
    public static void main(String[] args) throws IOException {
        int connectionPort = (args.length == 2) ? Integer.parseInt(args[1]) : 8030;
        BBModulePage modulePage = new BBModulePage(null, "localhost", connectionPort);
        HttpServer server = HttpServer.create(new InetSocketAddress(8020), 0);
        for(Map.Entry<String, HttpHandler> entry : modulePage.getModuleHandlers().entrySet()){
            server.createContext(entry.getKey(), entry.getValue());
        }
        server.start();
    }
}
