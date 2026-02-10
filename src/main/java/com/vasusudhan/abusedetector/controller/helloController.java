package com.vasusudhan.abusedetector.controller;

import com.vasusudhan.abusedetector.beans.countBean;
import com.vasusudhan.abusedetector.service.basicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class helloController {
    @Autowired
    private basicService basicService;
    @GetMapping("/")
    public AtomicInteger helloReturner(){
        return basicService.returnHello();
    }
    @GetMapping("/home")
    public String homeReturner(){return "Welcome to world";}
}

