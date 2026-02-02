package com.vasusudhan.abusedetector.storageclasses;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class inMem {
    HashMap<String,AtomicInteger> map=new HashMap<>();
    public void addtoMem(String address){
        if(map.get(address)==null){
            AtomicInteger temp=new AtomicInteger(0);
            map.put(address,temp);
        }
        else{
            map.put(address,new AtomicInteger(map.get(address).addAndGet(1)));
        }
    }
    public AtomicInteger getValue(String address){
        return map.get(address);
    }
}
