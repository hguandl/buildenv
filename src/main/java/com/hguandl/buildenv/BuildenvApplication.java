package com.hguandl.buildenv;

import com.hguandl.buildenv.util.Sandbox;
import com.hguandl.buildenv.util.SandboxConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@SpringBootApplication
public class BuildenvApplication {
    @PreDestroy
    public static void cleanup() {
        Sandbox.cleanup(true);
    }

    @PostConstruct
    public void setup() {
        Sandbox.setup(config.getLife(), config.getCnt());
    }

    public static void main(String[] args) {
        SpringApplication.run(BuildenvApplication.class, args);
    }


    public BuildenvApplication(SandboxConfig config) {
        this.config = config;
    }

    private final SandboxConfig config;
}
