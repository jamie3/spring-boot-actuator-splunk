package org.springframework.boot.actuator.splunk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class SplunkConfiguration {

    @Autowired
    private Environment env;

    @Bean
    public ActuatorPoller actuatorPoller() {
        ActuatorPoller actuatorPoller = new ActuatorPoller();
        actuatorPoller.setPoll(Long.parseLong(env.getProperty("springboot.actuator.splunk.port", "60000")));
        actuatorPoller.setServerPort(Integer.parseInt(env.getProperty("server.port", "8080")));
        actuatorPoller.setSourcetype(env.getProperty("springboot.actuator.splunk.sourcetype", "actuator"));
        actuatorPoller.setEventCollectorUrl(env.getRequiredProperty("springboot.actuator.splunk.eventCollectorUrl"));
        actuatorPoller.setEndpoints(env.getProperty("springboot.actuator.splunk.endpoints", "metrcs,health").split(","));
        actuatorPoller.setManagementContextPath(env.getProperty("springboot.actuator.splunk.management.context-path", ""));
        actuatorPoller.setAuthorization(env.getRequiredProperty("springboot.actuator.splunk.authorization"));
        return actuatorPoller;
    }
}
