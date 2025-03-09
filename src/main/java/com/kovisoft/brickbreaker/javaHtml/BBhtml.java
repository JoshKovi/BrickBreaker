package com.kovisoft.brickbreaker.javaHtml;

import com.kovisoft.brickbreaker.utils.HttpClientSetup;
import com.kovisoft.logger.Logger;
import com.kovisoft.servercommon.interfaces.StaticHtml;
import com.kovisoft.servercommon.utilities.FileUtilities;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.image.BufferedImage;

public class BBhtml extends StaticHtml {

    //BASE RESOURCES
    public String BASE_HTML = "";
    public String PNF_HTML = "";
    public String BASE_CSS = "";
    public String BASE_JS = "";

    public final String BB_RESOURCES = getResourcePath("web/brickbreaker");
    public String CANVAS_HTML = FileUtilities.getFileContent(BB_RESOURCES + "/canvas.html");

    public String CONTROLLER = FileUtilities.getFileContent(BB_RESOURCES + "/Controller.js");
    public String DISPLAY = FileUtilities.getFileContent(BB_RESOURCES + "/Display.js");
    public String ENGINE = FileUtilities.getFileContent(BB_RESOURCES + "/Engine.js");
    public String GAME_OBJECTS = FileUtilities.getFileContent(BB_RESOURCES + "/GameObjects.js");
    public String MOTION = FileUtilities.getFileContent(BB_RESOURCES + "/Motion.js");
    public String MAIN = FileUtilities.getFileContent(BB_RESOURCES + "/Main.js");

    public BufferedImage BASE_LOGO;
    public HashMap<String, String> resourceMap = new HashMap<>();
    protected boolean successfulGet;

    //Default Resources
    private static final String DEFAULT_HTML = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title>Brick Breaker</title>\n" +
            "    <meta name=\"author\" content=\"Joshua Kovacevich\">\n" +
            "</head>\n" +
            "<body><section id=\"content-body\">%s</section></body></html>";

    private static BBhtml bb;
    public static BBhtml getInstance(){
        if(bb == null){
            bb = new BBhtml();
        } else if(!bb.successfulGet){
            bb.successfulGet = bb.getRequiredResources();
            bb.updateResourceMap();
        }
        return bb;
    }

    private BBhtml(){
        successfulGet = getRequiredResources();
        updateResourceMap();
    }

    public static String getResourcePath(String resource){
        return getResourcePath(resource, BBhtml.class);
    }

    protected boolean getRequiredResources() {
        try{
            Map<String, String> resources = HttpClientSetup.getInstance()
                    .getResourcesFromMain(List.of("/BasePage.html", "/home.css", "/home.js", "BasicLogo.PNG", "/404.html"));
            BASE_HTML = resources.getOrDefault("/BasePage.html", DEFAULT_HTML);
            BASE_CSS = resources.getOrDefault("/home.css", "");
            BASE_JS = resources.getOrDefault("/home.js", "");
            PNF_HTML = resources.getOrDefault("/404.html", "");
            String base64Img = resources.getOrDefault("BasicLogo.PNG", "");
            if(base64Img.isBlank()) {
                replacePaths();
                BASE_LOGO = new BufferedImage(0,0,1);
                throw new NullPointerException("Img is empty!");
            }
            byte[] imgBytes = Base64.getDecoder().decode(base64Img);
            try(InputStream is = new ByteArrayInputStream(imgBytes)){
                BASE_LOGO = ImageIO.read(is);
            }
            replacePaths();
            return true;
        } catch (NullPointerException e){
            Logger.getLogger("BBLogger").exception().log("NPE during startup of BB!", e);
        } catch (Exception e) {
            Logger.getLogger("BBLogger").exception().log("Exception occurred trying to retrieve resources, using backup!", e);
            BASE_HTML = BASE_HTML.isBlank() ? DEFAULT_HTML : BASE_HTML;
        }
        return false;
    }

    protected void updateResourceMap() {
        resourceMap.put("", String.format(this.BASE_HTML, CANVAS_HTML));
        resourceMap.put("embedded", CANVAS_HTML);
        resourceMap.put("home.js", this.BASE_JS);
        resourceMap.put("home.css", this.BASE_CSS);
        resourceMap.put("Display.js", DISPLAY);
        resourceMap.put("GameObjects.js", GAME_OBJECTS);
        resourceMap.put("Motion.js", MOTION);
        resourceMap.put("Controller.js", CONTROLLER);
        resourceMap.put("Engine.js", ENGINE);
        resourceMap.put("Main.js", MAIN);
    }

    private void replacePaths(){
        BASE_HTML = BASE_HTML.replace("/home.js", "/brickbreaker?home.js");
        BASE_HTML = BASE_HTML.replace("/home.css", "/brickbreaker?home.css");
        BASE_HTML = BASE_HTML.replace("/img?imgTitle=BasicLogo", "/brickbreaker?BasicLogo.PNG");
    }

}
