package com.kovisoft.brickbreaker.handlers;

import com.kovisoft.brickbreaker.exports.records.LeaderBoard;
import com.kovisoft.brickbreaker.javaHtml.BBhtml;
import com.kovisoft.portwebdatabase.data.exports.DBManagerFactory;
import com.kovisoft.portwebdatabase.data.exports.DBOperations;
import com.kovisoft.servercommon.baseabstract.AbstractHandler;
import com.kovisoft.servercommon.utilities.RequestHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

public class LeaderBoardHandler extends AbstractHandler {

    private static final int LIMIT_RESULT = 50;
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        long start = System.currentTimeMillis();
        logRequest(exchange);
        DBOperations userOps = DBManagerFactory.getDBOperations("portfolio-user");
        if(userOps == null){
            logger.warn("This database is not initialized, not much to do on this page in that case.");
        }
        try{
            Map<String, String> urlParams = RequestHandler.getUrlParameters(exchange);
            int pageNum = Math.max(Integer.parseInt(urlParams.getOrDefault("page", "0")), 0);
            String content;

            if(userOps == null){
                content = String.format(BBhtml.getInstance().LEADER_HTML, "");
            } else if(urlParams.containsKey("page")){
                content = getLeaderEntries(userOps, pageNum);
            } else {
                content = String.format(BBhtml.getInstance().LEADER_HTML, getLeaderEntries(userOps, pageNum));
            }

            if(!urlParams.containsKey("nav") && !urlParams.containsKey("page")){
                content = String.format(BBhtml.getInstance().BASE_HTML, content);
            }

            sendResponse(exchange, content, "text/html", 200);
        } catch(Exception e){
            logger.except("Exception occurred while trying to serve LeaderBoard page: ", e);
            throw e;
        }
        logger.info("Served Leader Board content in " + (System.currentTimeMillis() - start) + "ms");
    }

    private String getLeaderEntries(DBOperations userOps, int pageNum) {
        List<LeaderBoard> entries = userOps.getAllEntriesDescending(LeaderBoard.class, LIMIT_RESULT, LIMIT_RESULT*pageNum, "score");
        if(entries == null || entries.isEmpty()) return "";
        StringBuilder entrySb = new StringBuilder();
        for(LeaderBoard entry : entries){
            entrySb.append(String.format(BBhtml.getInstance().LEADER_ENTRY,
                    entry.score(), entry.score(),
                    entry.player(), entry.player(),
                    entry.dateAchieved().toInstant(ZoneOffset.UTC).toEpochMilli(), entry.dateAchieved().toLocalDate(),
                    entry.location(), entry.location()));
        }
        return entrySb.toString();
    }
}
