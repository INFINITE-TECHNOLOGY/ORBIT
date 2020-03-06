package io.infinite.orbit

import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.hateoas.config.EnableHypermediaSupport

@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
@SpringBootApplication
@Slf4j
class App implements CommandLineRunner {

    static void main(String[] args) {
        SpringApplication.run(App.class, args)
    }

    @Override
    @BlackBox
    void run(String... args) throws Exception {
        log.debug("Started Orbit.")
    }

}
