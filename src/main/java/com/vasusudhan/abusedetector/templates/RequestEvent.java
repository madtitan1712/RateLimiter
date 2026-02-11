package com.vasusudhan.abusedetector.templates;

import lombok.Data;

@Data
public class RequestEvent {
    String Ip;
    String URL;
    String Method;
    long timestamp;
    int responseStatus;
    boolean blocked;
    long responseTimeMS;
    public RequestEvent(String URL, String ip, String method, long timestamp, int responseStatus, boolean blocked, long responseTimeMS) {
        this.URL = URL;
        Ip = ip;
        Method = method;
        this.timestamp = timestamp;
        this.responseStatus = responseStatus;
        this.blocked = blocked;
        this.responseTimeMS = responseTimeMS;
    }
}
