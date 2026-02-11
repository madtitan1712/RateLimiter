package com.vasusudhan.abusedetector.templates;

import lombok.Data;
import lombok.Getter;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;
public class ratelimitState {
    Deque<Long>DateTime;
    @Getter
    AtomicInteger allowedCount;
    public ratelimitState(){
        DateTime=new ConcurrentLinkedDeque<>();
        allowedCount=new AtomicInteger(0);
    }
    public void incrementCount(){
        allowedCount.incrementAndGet();
    }
    public AtomicInteger getCount(){
        return allowedCount;
    }
    public void setDateTime(long dateTime){
        DateTime.add(dateTime);
    }
    public Deque<Long> getDateTime(){
        return DateTime;
    }
    public void clearOldTimestamps(long windowMS){
        long currMS=System.currentTimeMillis();
        DateTime.removeIf(k-> k<currMS-windowMS);
    }
    public long requestCount(long windowMS){
        clearOldTimestamps(windowMS);
        return DateTime.size();
    }
}

