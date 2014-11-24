package com.proptiger.seo.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.WebApplicationInitializer;

import com.proptiger.core.config.WebInitializer;
import com.proptiger.core.config.WebMvcConfig;

/**
 * Initialize web application, configuring front controller DispatcherServlet,
 * this file is replacement of web.xml
 * 
 * @author Rajeev Pandey
 * @author azi
 * 
 */
public class AppWebInitializer extends WebInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onServletStartup(servletContext, WebMvcConfig.class);
    }
}