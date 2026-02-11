package com.vasusudhan.abusedetector.storageclasses;

import com.vasusudhan.abusedetector.templates.RequestEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
@Component
public class RequestEventQueue {
    private static final Deque<RequestEvent> requestEventQueue=new ConcurrentLinkedDeque<>();
    private static final int MAX_SIZE=1000;
    public void addRequestEvent(RequestEvent requestEvent){
        requestEventQueue.add(requestEvent);
        System.out.println("Called function!!");
        if(requestEventQueue.size()>MAX_SIZE){
            requestEventQueue.removeFirst();
        }
    }
    public ArrayList<RequestEvent> getQueue(){
        return new ArrayList<>(requestEventQueue);
    }
    public void clearQueue() {
        requestEventQueue.clear();
    }
}
