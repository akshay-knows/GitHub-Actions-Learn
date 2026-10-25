package com.swiftyquote.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
// Open http://localhost:8761 to see registered services.
@EnableEurekaServer
@SpringBootApplication
public class DiscoveryApplication { public static void main(String[] args) { SpringApplication.run(DiscoveryApplication.class,args); } }
