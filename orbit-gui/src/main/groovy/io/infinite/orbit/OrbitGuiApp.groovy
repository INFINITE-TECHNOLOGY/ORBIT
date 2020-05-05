package io.infinite.orbit

import groovy.swing.SwingBuilder
import groovy.ui.ConsoleActions
import groovy.ui.ConsoleView
import groovy.util.logging.Slf4j
import io.infinite.ascend.granting.client.authentication.ClientJwtPreparator
import io.infinite.ascend.granting.client.services.ClientAuthorizationGrantingService
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

import javax.swing.*
import java.awt.Window

@BlackBox(level = CarburetorLevel.METHOD)
@Slf4j
@SpringBootApplication
@ComponentScan(
        basePackages = [
                "io.infinite.ascend.common",
                "io.infinite.ascend.granting",
                "io.infinite.orbit"
        ],
        excludeFilters = [
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = ClientJwtPreparator.class)
        ])
@EnableJpaRepositories([
        "io.infinite.ascend",
        "io.infinite.orbit"
])
@EntityScan([
        "io.infinite.ascend",
        "io.infinite.orbit"
])
class OrbitGuiApp implements ApplicationRunner {

    String ascendClientPublicKeyName

    String ascendGrantingUrl

    String scopeName

    String authorizationServerNamespace

    String authorizationClientNamespace

    @Autowired
    ClientAuthorizationGrantingService clientAuthorizationGrantingService

    SwingBuilder swing

    RootPaneContainer frame

    static void main(String[] args) {
        System.setProperty("jwtAccessKeyPublic", "")
        System.setProperty("jwtAccessKeyPrivate", "")
        System.setProperty("jwtRefreshKeyPublic", "")
        System.setProperty("jwtRefreshKeyPrivate", "")
        System.setProperty("ascendValidationUrl", "")
        System.setProperty("orbitUrl", "")
        SpringApplicationBuilder builder = new SpringApplicationBuilder(OrbitGuiApp.class)
        builder.headless(false)
        ConfigurableApplicationContext context = builder.run(args)
    }

    @Override
    void run(ApplicationArguments args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
        new GroovyMainForm().show()
    }

}
