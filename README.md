# Spring Boot Actuator Splunk

Enables your Spring Boot application to send its Actuator data to Splunk's HTTP Event Collector API.

http://dev.splunk.com/view/event-collector/SP-CAAAE6M

http://docs.spring.io/spring-boot/docs/current/reference/html/production-ready.html

## Usage

Clone and build the project then add the jar to your Maven or Gradle local repo (note: This is not available on Maven Central). Finally add the dependency to your Spring Boot project dependencies.

Maven:

```
<dependency>
    <group>org.springframework.boot</group>
    <artifactId>spring-boot-actuator-splunk</artifactId>
    <version>1.4.1.RELEASE</version>
</dependency>
```

Gradle:

```
compile ('org.springframework.boot:spring-boot-actuator-splunk:1.4.1.RELEASE')
```

If you are using a newer (or older) version of Spring Boot just modify the version in the `build.gradle` file and re-build.

## Build

```
gradlew build
```

## Configuration

Add the following to your Spring Boot `application.properties` file, or just make these available
via System property.

```
# Port of the actuator endpoint
server.port=8080

# Time between polling the actuator endpoint
springboot.actuator.splunk.poll=30000

# The sourcetype for the log message that is sent to Splunk
springboot.actuator.splunk.sourcetype=myservice

# Destination HTTP url for sending events to Splunk
springboot.actuator.splunk.eventCollectorUrl=http://splunk:8090/services/collector/event

# Spring Boot management endpoint base context path
springboot.actuator.splunk.management.context-path=/manage

# Spring Boot management endpoints in which to poll (hint, you can add any endpoint, not limited to Spring Boot Actuator)
springboot.actuator.splunk.endpoints=health,metrics

# Splunk HTTP Event Collector authorization key
springboot.actuator.splunk.authorization=Splunk AA-BB-CC-DD


```
    
## Troubleshooting
    
When the service starts you should see the following in the log `Spring Boot Actuator Splunk enabled`

If you application is not annotated with `@SpringBootApplication` then you will need to add `@ComponentScan("com.springframework.boot.actuator.splunk")`.


```
@EnableAutoConfiguration
@ComponentScan("com.myapplication")
@ComponentScan("org.springframework.boot.actuator.splunk")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

    
## Limitations

Currently this is in beta mode and only polls the Actuator endpoint using HTTP and no authentication.

## Known Issues

None thus far. Please report any bugs :)

## Contributions

Contributions are welcome. Please send me any merge requests.