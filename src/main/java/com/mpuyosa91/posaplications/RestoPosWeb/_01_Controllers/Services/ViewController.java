package com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Services;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {

    @RequestMapping(path = "/home")
    public String home() {
        return "index";
    }

}
