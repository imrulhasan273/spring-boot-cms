package com.spring.cms.ApiController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class Home {

    @GetMapping
    public String home(){
        return "Apllication is working!!! " + new Date();
    }

}
