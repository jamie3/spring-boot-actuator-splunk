package org.springframework.boot.actuator.splunk;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Polls Spring Actuator endpoint and sends information to Splunk's HTTP Event Collector
 */
@Component
public class ActuatorPoller {

    static final Logger LOG = LoggerFactory.getLogger(ActuatorPoller.class);

    private int serverPort;

    private long poll;

    private String sourcetype;

    private String eventCollectorUrl;

    private String[] endpoints;

    private String managementContextPath;

    private String authorization;

    private Thread thread;

    @PostConstruct
    public void init() {

        LOG.info("Spring Boot Actuator Splunk enabled");

        thread = new Thread(new Runnable() {

            @Override
            public void run() {

                RestTemplate restTemplate = new RestTemplate();

                while (thread != null) {

                    try {
                        Thread.sleep(10000L);
                    } catch (InterruptedException e) {
                        break;
                    }

                        // do http request to spring boot actuator
                    for (String endpoint : endpoints) {
                        try {
                            final String url = "http://localhost:" + serverPort + "/" + managementContextPath + "/" + endpoint;
                            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
                            if (response != null) {
                                if (response.getStatusCodeValue() == 200) {
                                    Map event = response.getBody();

                                    Map splunkEvent = new HashMap();
                                    splunkEvent.put("time", System.currentTimeMillis());
                                    splunkEvent.put("host", InetAddress.getLocalHost().getHostName());
                                    splunkEvent.put("source", "HEC");
                                    splunkEvent.put("sourcetype", "actuator");
                                    splunkEvent.put("event", event);

                                    String json = new Gson().toJson(splunkEvent);

                                    HttpHeaders headers = new HttpHeaders();
                                    headers.put("Authorization", Arrays.asList(authorization));
                                    headers.put("Content-Type", Arrays.asList("application/json"));
                                    HttpEntity<String> entity = new HttpEntity<String>(json, headers);

                                    // send to splunk http event collector
                                    ResponseEntity<Object> responseSplunk = restTemplate.postForEntity(eventCollectorUrl, entity, Object.class);

                                    if (responseSplunk.getStatusCodeValue() == 200) {

                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();;
                        }
                    }
                }
            }
        });

        thread.start();
    }

    @PreDestroy
    public void destroy() {
        if (thread != null) {
            if (!thread.isInterrupted()) {
                thread.interrupt();
                thread = null;
            }
        }
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public long getPoll() {
        return poll;
    }

    public void setPoll(long poll) {
        this.poll = poll;
    }

    public String getSourcetype() {
        return sourcetype;
    }

    public void setSourcetype(String sourcetype) {
        this.sourcetype = sourcetype;
    }

    public String getEventCollectorUrl() {
        return eventCollectorUrl;
    }

    public void setEventCollectorUrl(String eventCollectorUrl) {
        this.eventCollectorUrl = eventCollectorUrl;
    }

    public String[] getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(String[] endpoints) {
        this.endpoints = endpoints;
    }

    public String getManagementContextPath() {
        return managementContextPath;
    }

    public void setManagementContextPath(String managementContextPath) {
        this.managementContextPath = managementContextPath;
    }
}
