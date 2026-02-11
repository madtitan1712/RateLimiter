package com.vasusudhan.abusedetector.beans;

import com.vasusudhan.abusedetector.storageclasses.inMem;
import com.vasusudhan.abusedetector.templates.ratelimitState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class rateLimitEngine {
    @Autowired
    private inMem MemoryHandler;
    public boolean checkIfAllowed(String IP,String Endpoint,long maxRequests,long windowMs){
        ratelimitState currState= MemoryHandler.fetchDetails(IP,Endpoint);
        long count = currState.requestCount(windowMs);
        System.out.println("Request count in window: " + count + " | Max allowed: " + maxRequests);
        if(currState.requestCount(windowMs)>maxRequests){
            return false;
        }
        else {
            return true;
        }
    }
}
