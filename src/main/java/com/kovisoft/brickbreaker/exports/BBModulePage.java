package com.kovisoft.brickbreaker.exports;

import com.kovisoft.brickbreaker.exports.records.LeaderBoard;
import com.kovisoft.brickbreaker.handlers.BrickBreakerHandler;
import com.kovisoft.brickbreaker.handlers.LeaderBoardHandler;
import com.kovisoft.brickbreaker.utils.HttpClientSetup;
import com.kovisoft.logger.exports.LoggerFactory;
import com.kovisoft.pg.database.data.exports.AbstractMigration;
import com.kovisoft.servercommon.interfaces.ModulePage;
import com.sun.net.httpserver.HttpHandler;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BBModulePage implements ModulePage {

    public BBModulePage(SSLContext sslContext, String host, int port){
        try{
            LoggerFactory.createLogger(System.getProperty("user.dir") +"/logs", "BBLogger");
        } catch (IOException e) {
            throw new RuntimeException("Could not create BBLogger", e);
        }

        HttpClientSetup.createInstance(sslContext, host, port);

    }
    @Override
    public HashMap<String, HttpHandler> getModuleHandlers() {
        return new HashMap<>(){{
           put("/brickbreaker", new BrickBreakerHandler());
           put("/brickbreaker/leaderboard", new LeaderBoardHandler());
        }};
    }

    @Override
    public Map<Class<?>, String> getModuleRecords(){
        return new HashMap<>(){{
            put(LeaderBoard.class, "GRANT SELECT, INSERT ON TABLE ");
        }};
    }

    @Override
    public AbstractMigration getMostRecentMigration(){
        return new FirstBBMigration();
    }
}
