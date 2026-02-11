package com.vasusudhan.abusedetector.filters;

import com.vasusudhan.abusedetector.beans.countBean;
import com.vasusudhan.abusedetector.beans.rateLimitEngine;
import com.vasusudhan.abusedetector.storageclasses.inMem;
import com.vasusudhan.abusedetector.templates.requestTemplate;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static Logger logger = LoggerFactory.getLogger(basicFilter.class);
    @Autowired
    private HttpServletResponse httpServletResponse;
    @Autowired
    private rateLimitEngine rateLimitEngine;
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
        requestTemplate details=handleRequest((HttpServletRequest)request);
        if(!MemoryStorage.checkAndAdd(details.getIPaddress(), details.getUrl(), MAXRQ, windowMS)){
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpServletResponse.setContentType("application/json");
            httpServletResponse.getWriter().write("Too many requests");
            return ;
        }
        chain.doFilter(request,response);
    }
}
