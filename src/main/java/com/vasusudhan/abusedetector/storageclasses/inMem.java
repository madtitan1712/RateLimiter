package com.vasusudhan.abusedetector.storageclasses;

import com.vasusudhan.abusedetector.templates.ratelimitState;
import com.vasusudhan.abusedetector.templates.requestTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class inMem {
    private final ConcurrentHashMap<String, ratelimitState> map = new ConcurrentHashMap<>();

    public void addtoMemnew(requestTemplate request) {
        String key=request.getIPaddress()+"|"+request.getUrl();
        ratelimitState state=map.computeIfAbsent(key,k-> new ratelimitState());
        state.incrementCount();
        state.setDateTime(System.currentTimeMillis());
    }

    public ratelimitState fetchDetails(String Ip,String Url) {
        String key=Ip+"|"+Url;
        return map.computeIfAbsent(key,k-> new ratelimitState());
    }

    public synchronized boolean checkAndAdd(String ip, String endpoint, long maxRequests, long windowMs) {
        String key = ip + "|" + endpoint;
        ratelimitState state = map.computeIfAbsent(key, k -> new ratelimitState());
        if (state.requestCount(windowMs) >= maxRequests) {
            return false;
        }
        state.setDateTime(System.currentTimeMillis());
        return true;
    }
}
