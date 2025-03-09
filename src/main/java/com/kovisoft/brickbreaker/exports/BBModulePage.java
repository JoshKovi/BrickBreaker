package com.kovisoft.brickbreaker.exports;

import com.kovisoft.brickbreaker.handlers.BrickBreakerHandler;
import com.kovisoft.brickbreaker.utils.HttpClientSetup;
import com.kovisoft.servercommon.interfaces.ModulePage;
import com.kovisoft.logger.Logger;
import com.sun.net.httpserver.HttpHandler;

import javax.net.ssl.SSLContext;
import java.util.HashMap;

public class BBModulePage implements ModulePage {

    public BBModulePage(SSLContext sslContext, String host, int port){
        Logger.createLogger(System.getProperty("user.dir") +"/logs", "BBLogger", "BBLogger");
        HttpClientSetup.createInstance(sslContext, host, port);
    }
    @Override
    public HashMap<String, HttpHandler> getModuleHandlers() {
        return new HashMap<>(){{
           put("/brickbreaker", new BrickBreakerHandler());
        }};
    }
}
