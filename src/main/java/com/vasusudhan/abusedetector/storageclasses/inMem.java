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

    public void fetchDetails(requestTemplate details) {
        String key=details.getIPaddress()+"|"+details.getUrl();
        ratelimitState state=map.computeIfAbsent(key,k-> new ratelimitState());
        System.out.println(details.getIPaddress()+" "+details.getUrl()+" "+details.getRequestType()+" "+state.getCount());
    }
}
