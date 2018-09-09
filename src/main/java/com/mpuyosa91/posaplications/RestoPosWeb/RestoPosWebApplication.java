package com.mpuyosa91.posaplications.RestoPosWeb;

import com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Place.GeneralController;
import com.mpuyosa91.posaplications.RestoPosWeb._02_Views.StarterWindow;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.util.Date;
import java.util.TimeZone;

@SpringBootApplication
public class RestoPosWebApplication {

    @PostConstruct
    public void init(){
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));   // It will set UTC timezone
    }

    public static void main(String[] args) {

        ConfigurableApplicationContext
                ctx =
                new SpringApplicationBuilder(RestoPosWebApplication.class).headless(false).run(args);
        GeneralController.springReady = true;

        EventQueue.invokeLater(() -> {
            StarterWindow starterWindow = new StarterWindow();
            starterWindow.run();
        });
    }
}
