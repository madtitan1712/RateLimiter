package com.vasusudhan.abusedetector.controller;

import com.vasusudhan.abusedetector.beans.countBean;
import com.vasusudhan.abusedetector.service.basicService;
import com.vasusudhan.abusedetector.storageclasses.RequestEventQueue;
import com.vasusudhan.abusedetector.templates.RequestEvent;
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
    @Autowired
    private RequestEventQueue requestEventQueue;
    @GetMapping("/debug/events")
    public java.util.Map<String, Object> getEvents() {
        java.util.ArrayList<RequestEvent> events = requestEventQueue.getQueue();
        return java.util.Map.of(
                "totalEvents", events.size(),
                "events", events
        );
    }
}

