package com.kovisoft.brickbreaker.utils;

import com.kovisoft.logger.Logger;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClientSetup {
    private final SSLContext context;
    private final String host;
    private final int port;
    private static Logger logger = Logger.getLogger("BBLogger");
    private static HttpClientSetup clientSetup;
    public static HttpClientSetup getInstance(){
        if(clientSetup == null) throw new RuntimeException("HttpClientSetup must be initialized with createInstance before getting instance");
        return clientSetup;
    }
    public static void createInstance(SSLContext sslContext, String host, int port){
        logger.info().log(String.format("Setting up client with ssl: %b, host: %s, port: %d",
                        !(sslContext == null), host, port));
        clientSetup = new HttpClientSetup(sslContext, host, port);
    }
    private HttpClientSetup(SSLContext sslContext, String host, int port){
        this.context = sslContext;
        this.host = host;
        this.port = port;
    }

    private HttpClient getHttpClient(){
        if(context == null){
            logger.error().log("SSL Context null building a HTTP server for localhost! Ignore if this is intended.");
            return HttpClient.newHttpClient();
        }
        else {
            logger.log().log("SSL Context not null!");
            return HttpClient.newBuilder().sslContext(context).build();
        }
    }



    public Map<String, String> getResourcesFromMain(List<String> resources) throws URISyntaxException, IOException, InterruptedException {
        HashMap<String, String> resourceMap = new HashMap<>();
        if(resources.isEmpty()){return resourceMap;}
        HttpClient client = getHttpClient();
        String requestString = (context == null) ?
                String.format("http://%s:%d/api/v1/portfolio?%s", host, port, String.join("&", resources)) :
                "https://www.kovisoft.net/api/v1/portfolio?" + String.join("&", resources);
        Logger.getLogger("BBLogger").info().log("Attempting API Call to: " + requestString);
        HttpRequest request = HttpRequest.newBuilder(new URI(requestString)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String[] files = response.body().split(logger.getLineDelimiter());
        if(files.length != resources.size()){
            logger.error().log(String.format("Response length do not equal resource length! resources [%d], files[%d]",
                    resources.size(), files.length));
            logger.error().log("Resources: " + String.join(", ", resources));
            logger.error().log("Files: " + String.join(", ",
                    Arrays.stream(files).map(file -> file.substring(0, 64).replaceAll("\n", logger.getLineDelimiter())).toList()));

        }
        for(int i = 0; i < files.length; i++){
            String file = files[i];
            String[] split = file.split("=", 2);
            if(split.length == 2){
                resourceMap.put(split[0], split[1].equals("NOT_FOUND") ? "" : split[1]);
                logger.info().log(split[0] + " : " + split[1].substring(0, 64).
                        replaceAll("\n", logger.getLineDelimiter()));
            }
        }

        client.close();
        return resourceMap;

    }
}
