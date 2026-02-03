package com.vasusudhan.abusedetector.storageclasses;

import com.vasusudhan.abusedetector.templates.requestTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class inMem {
    HashMap<String,requestTemplate> map=new HashMap<>();
    public void addtoMemnew(requestTemplate request){
       if(map.get(request.getIPaddress())==null){
           map.put(request.getIPaddress(),request);
       }
       else{
           request.setCount(new AtomicInteger(request.getCount().addAndGet(1)));
           map.put(request.getIPaddress(),request);
       }
    }
    public requestTemplate getValue(String address){
        return map.get(address);
    }
}
