package com.vasusudhan.abusedetector.filters;
import com.vasusudhan.abusedetector.beans.rateLimitEngine;
import com.vasusudhan.abusedetector.storageclasses.RequestEventQueue;
import com.vasusudhan.abusedetector.storageclasses.inMem;
import com.vasusudhan.abusedetector.templates.RequestEvent;
import com.vasusudhan.abusedetector.templates.requestTemplate;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1)
public class basicFilter implements Filter{
    private static long MAXRQ=10;
    private static long windowMS=10000;
    @Autowired
    private inMem MemoryStorage;
    @Autowired
    private HttpServletResponse httpServletResponse;
    @Autowired
    private rateLimitEngine rateLimitEngine;
    @Autowired
    private RequestEventQueue  requestEventQueue;
    private boolean CheckForAccess(String Ip,String Url){
      return rateLimitEngine.checkIfAllowed(Ip,Url,MAXRQ,windowMS);
    };
    private requestTemplate handleRequest(HttpServletRequest request){
        // Store only the endpoint path (no scheme/host/port/query)
        String endpoint = request.getRequestURI(); // includes context path; use ge tServletPath() if you want path within the app only
        return new requestTemplate(request.getRemoteAddr(), request.getMethod(), endpoint);
    }
    //Currently this can just add the IP address and the visit count of the IP.
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        long currentTime=System.currentTimeMillis();
        requestTemplate details=handleRequest((HttpServletRequest)request);
        if(!MemoryStorage.checkAndAdd(details.getIPaddress(), details.getUrl(), MAXRQ, windowMS)){
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpServletResponse.setContentType("application/json");
            httpServletResponse.getWriter().write("Too many requests");
            int code=403;
            RequestEvent denied=new RequestEvent(details.getUrl(),details.getIPaddress(),details.getRequestType(),currentTime,code,true,System.currentTimeMillis()-currentTime);
            requestEventQueue.addRequestEvent(denied);
            return ;
        }
        chain.doFilter(request,response);
        long processTime=System.currentTimeMillis()-currentTime;
        HttpServletResponse httpServletResponse=(HttpServletResponse)response;
        int ResponseCode=httpServletResponse.getStatus();
        if(ResponseCode==0){
            ResponseCode=httpServletResponse.SC_OK;
        }
        RequestEvent processed=new RequestEvent(details.getUrl(), details.getIPaddress(), details.getRequestType(),currentTime,ResponseCode,false,processTime);
        requestEventQueue.addRequestEvent(processed);
    }
}
