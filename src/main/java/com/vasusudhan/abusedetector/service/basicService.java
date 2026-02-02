package com.vasusudhan.abusedetector.service;

import com.vasusudhan.abusedetector.beans.countBean;
import com.vasusudhan.abusedetector.storageclasses.inMem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class basicService {
    @Autowired
    private countBean Countbean;
    public AtomicInteger returnHello(){
        return Countbean.returnNum();
    }
}
