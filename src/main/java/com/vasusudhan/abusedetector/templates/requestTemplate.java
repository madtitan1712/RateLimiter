package com.vasusudhan.abusedetector.templates;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;
@Data
public class requestTemplate {
    private AtomicInteger count = new AtomicInteger(1);
    private String IPaddress;
    private String RequestType;
    private String Url;
    public requestTemplate(String IPaddress, String RequestType, String Url) {
        this.IPaddress = IPaddress;
        this.RequestType = RequestType;
        this.Url = Url;
    }
}
