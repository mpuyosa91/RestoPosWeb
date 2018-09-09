package com.mpuyosa91.posaplications.RestoPosWeb;

import com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Place.GeneralController;
import com.mpuyosa91.posaplications.RestoPosWeb._02_Views.StarterWindow;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.awt.*;

@SpringBootApplication
public class RestoPosWebApplication {

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
