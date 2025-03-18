package com.kovisoft.brickbreaker.handlers;

import com.kovisoft.brickbreaker.exports.records.LeaderBoard;
import com.kovisoft.pg.database.data.exports.DBManagerFactory;
import com.kovisoft.servercommon.baseabstract.AbstractHandler;
import com.kovisoft.brickbreaker.javaHtml.BBhtml;
import com.kovisoft.servercommon.utilities.RequestHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BrickBreakerHandler extends AbstractHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        logRequest(exchange);
        logger.info("User accessing brickbreaker!");
        BBhtml bb = BBhtml.getInstance();
        logRequest(exchange);
        HashMap<String, String> params = RequestHandler.getUrlParameters(exchange);
        if(params.size() > 1){
            sendResponse(exchange, bb.PNF_HTML, "text/html", 404);
            return;
        }
        Optional<String> keyOpt = params.keySet().stream().findFirst();
        String key = keyOpt.orElse("");
        if(key.equals("BasicLogo.PNG")){
            sendResponse(exchange, bb.BASE_LOGO, "Logo");
        } else if(bb.resourceMap.containsKey(key)){
            String contentType = (key.endsWith("js")) ? "text/javascript" :
                    key.endsWith("css") ? "text/css" : "text/html";
            sendResponse(exchange, bb.resourceMap.get(key), contentType, 200);
        } else if(key.equals("lbEntry")){
            Map<String, Object> leaderEntry = RequestHandler.parseRequestBody(exchange).get("LeaderBoard-Entry").getFirst();
            try {
                leaderEntry.put("dateAchieved", LocalDateTime.now());
                leaderEntry.remove("id");
                LeaderBoard lb = new LeaderBoard(leaderEntry);
                LeaderBoard lbDb = DBManagerFactory.getDBOperations("portfolio-user").addRecord(lb);
                logger.info(String.format("Sent %s to db and got back %s from the add attempt", lb, lbDb));
                exchange.sendResponseHeaders(200, -1);
            } catch (NullPointerException npe){
                logger.warn("Looks like the db is probably not setup and connected properly!", npe);
            } catch (Exception e){
                logger.except("Looks like the user might have tried something the shouldn't have!", e);
            }
        } else {
            sendResponse(exchange, bb.PNF_HTML, "text/html", 404);
        }
    }
}
