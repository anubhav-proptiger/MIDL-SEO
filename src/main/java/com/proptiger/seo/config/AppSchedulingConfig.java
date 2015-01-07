package com.proptiger.seo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.proptiger.core.config.scheduling.QuartzSchedulerConfigurer;

/**
 * Enable scheduling and async capability in project.
 * 
 * @author Rajeev Pandey
 * 
 */
@Configuration
@EnableAsync
@EnableScheduling
public class AppSchedulingConfig /*extends QuartzSchedulerConfigurer*/{

}
