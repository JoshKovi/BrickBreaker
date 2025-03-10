package com.kovisoft.brickbreaker.handlers;

import com.kovisoft.logger.exports.LoggerFactory;
import com.kovisoft.servercommon.baseabstract.AbstractHandler;
import com.kovisoft.brickbreaker.javaHtml.BBhtml;
import com.kovisoft.servercommon.utilities.RequestHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

public class BrickBreakerHandler extends AbstractHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        logger.info("User accessing brickbreaker!");
        BBhtml bb = BBhtml.getInstance();
        logRequest(exchange);
        HashMap<String, String> params = RequestHandler.getUrlParameters(exchange);
        if(params.size() > 1){
            sendResponse(exchange, bb.PNF_HTML, "text/html", 404);
            return;
        }
        Optional<String> keyOpt = params.keySet().stream().findFirst();
        String key = (keyOpt.isPresent()) ? keyOpt.get() : "";
        if(key.equals("BasicLogo.PNG")){
            sendResponse(exchange, bb.BASE_LOGO, "Logo");
        } else if(bb.resourceMap.containsKey(key)){
            String contentType = (key.endsWith("js")) ? "text/javascript" :
                    key.endsWith("css") ? "text/css" : "text/html";
            sendResponse(exchange, bb.resourceMap.get(key), contentType, 200);
        } else {
            sendResponse(exchange, bb.PNF_HTML, "text/html", 404);
        }
    }
}
