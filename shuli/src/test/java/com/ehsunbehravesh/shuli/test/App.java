package com.ehsunbehravesh.shuli.test;

import com.ehsunbehravesh.shuli.application.Application;
import com.ehsunbehravesh.shuli.exception.ApplicationException;
import com.ehsunbehravesh.shuli.resource.HTMLResource;
import com.ehsunbehravesh.shuli.resource.JPGImageResource;
import com.ehsunbehravesh.shuli.resource.Resource;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 *
 * @author Ehsun Behravesh
 */
public class App extends Application {

    @Override
    protected void setup() throws ApplicationException, IOException {
        //InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("shuli1.properties");
        //loadConfig(is);
        //setPort(54);        
        //setContext("/hello");

        HTMLResource res1 = new HTMLResource(new File("/home/ehsun7b/1.html"));
        addResource("/", res1);        
        
        Resource resImage = new JPGImageResource(new File("/home/ehsun7b/Pictures/wallpaper/651960-1366x768.jpg"));
        addResource("/1.jpg", resImage);
    }

    public static void main(String[] args) throws ApplicationException, MalformedURLException, IOException {
        App app = new App();

        app.start();
    }    

}
