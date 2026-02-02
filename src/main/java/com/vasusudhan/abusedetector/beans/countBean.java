package com.vasusudhan.abusedetector.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class countBean {
    AtomicInteger count = new AtomicInteger(0);
    public AtomicInteger returnNum(){
        return count;
    }
    public void addValue(){
        count.incrementAndGet();
    }
}
