package com.vasusudhan.abusedetector.filters;

import com.vasusudhan.abusedetector.beans.countBean;
import com.vasusudhan.abusedetector.storageclasses.inMem;
import com.vasusudhan.abusedetector.templates.requestTemplate;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1)
public class basicFilter implements Filter{
    @Autowired
    private countBean CountBean;
    @Autowired
    private inMem currmodule;
    private static Logger logger = LoggerFactory.getLogger(basicFilter.class);
    private requestTemplate handleRequest(HttpServletRequest request){
        return new requestTemplate(request.getRemoteAddr(),request.getMethod(),request.getRequestURI());
    }
    //Currently this can just add the IP address and the visit count of the IP.
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("Basic Filter");
        System.out.println("Request Address: "+request.getRemoteAddr());
        System.out.println("Host Address: "+request.getRemoteHost());
        requestTemplate details=handleRequest((HttpServletRequest)request);
        currmodule.addtoMemnew(details);
        System.out.println("Current Request: "+details.getIPaddress()+" "+currmodule.getValue(details.getIPaddress()).getCount());
        chain.doFilter(request,response);
    }
}
