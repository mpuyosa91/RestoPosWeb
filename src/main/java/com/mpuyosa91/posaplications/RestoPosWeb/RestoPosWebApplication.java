package com.mpuyosa91.posaplications.RestoPosWeb;

import com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Place.GeneralController;
import com.mpuyosa91.posaplications.RestoPosWeb._02_Views.StarterWindow;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestoPosWebApplication {

    public static void main(String[] args) {
        (new Thread(new StarterWindow())).start();
        SpringApplication.run(RestoPosWebApplication.class, args);
        GeneralController.springReady = true;
    }
}
