package com.swiftyquote.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
// Open http://localhost:9090 to inspect service health and logs.
@EnableAdminServer
@SpringBootApplication
public class AdminApplication { public static void main(String[] args) { SpringApplication.run(AdminApplication.class,args); } }
