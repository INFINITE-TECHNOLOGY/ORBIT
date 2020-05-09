package io.infinite.orbit.gui

import groovy.swing.SwingBuilder
import groovy.util.logging.Slf4j
import io.infinite.ascend.common.entities.Authorization
import io.infinite.ascend.common.repositories.AuthorizationRepository
import io.infinite.ascend.common.repositories.RefreshRepository
import io.infinite.ascend.granting.client.services.ClientAuthorizationGrantingService
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

import javax.annotation.PostConstruct
import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.util.concurrent.LinkedBlockingQueue

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
                //@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = ClientJwtPreparator.class)
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

    static OrbitGuiApp instance

    String ascendClientPublicKeyName

    @Value('${ascendGrantingUrl}')
    String ascendGrantingUrl

    String scopeName

    String authorizationServerNamespace

    String authorizationClientNamespace

    @Autowired
    ClientAuthorizationGrantingService clientAuthorizationGrantingService

    @Autowired
    AuthorizationRepository authorizationRepository

    @Autowired
    RefreshRepository refreshRepository

    Authorization adminScopeAuthorization

    SwingBuilder swingBuilder = new SwingBuilder()

    RootPaneContainer frame

    JPanel adminPanel = new JPanel()

    JLabel unauthorizedMessageLabel = new JLabel("Sorry there is an authorization error.")

    JPanel anonymousPanel = new JPanel().add(new JLabel("Please wait while we log you in...")).parent as JPanel

    JFrame mainFrame = new JFrame(
            name: "mainFrame",
            title: "Infinite Technology ∞ Orbit Admin",
            visible: true,
            defaultCloseOperation: JFrame.EXIT_ON_CLOSE,
            size: new Dimension(1366, 768),
            locationRelativeTo: null,
            contentPane: anonymousPanel
    )

    JPanel unauthorizedPanel = new JPanel()

    Boolean scanAuthorization = false

    static void main(String[] args) {
        System.setProperty("jwtAccessKeyPublic", "")
        System.setProperty("jwtAccessKeyPrivate", "")
        System.setProperty("jwtRefreshKeyPublic", "")
        System.setProperty("jwtRefreshKeyPrivate", "")
        System.setProperty("ascendValidationUrl", "")
        System.setProperty("ascendClientPublicKeyName", "")
        System.setProperty("ascendClientPrivateKey", "")
        SpringApplicationBuilder builder = new SpringApplicationBuilder(OrbitGuiApp.class)
        builder.headless(false)
        ConfigurableApplicationContext context = builder.run(args)
    }

    void showPanel(JPanel jPanel) {
        SwingUtilities.invokeLater(new Runnable() {
            void run() {
                mainFrame.contentPane = jPanel
                jPanel.revalidate()
                mainFrame.repaint()
            }
        })
    }

    void unauthorized(String message) {
        unauthorizedMessageLabel.text = message
        unauthorized()
    }

    void unauthorized() {
        scanAuthorization = false
        showPanel(unauthorizedPanel)
    }

    void retryAuthorization() {
        showPanel(anonymousPanel)
        Thread.start {
            authorized(adminPanel)
        }
    }

    void authorized(JPanel panel) {
        try {
            adminScopeAuthorization = clientAuthorizationGrantingService.grantByScope("adminScope", ascendGrantingUrl, "global", "OrbitSaaS")
            log.debug("Authorized", adminScopeAuthorization)
            showPanel(panel)
            scanAuthorization = true
        } catch (Exception e) {
            unauthorized(e.getMessage())
        }
    }

    @Override
    void run(ApplicationArguments args) throws Exception {
        OrbitGuiApp.instance = this
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
        init()
        Thread.start {
            authorized(adminPanel)
        }
    }

    void init() {
        adminPanel.add(new JLabel("Welcome to Admin Panel."))
        adminPanel.add(swingBuilder.button(
                text: "Log out",
                actionPerformed: {
                    logout()
                }
        ))
        unauthorizedPanel.add(unauthorizedMessageLabel)
        unauthorizedPanel.add(swingBuilder.button(
                text: "Retry authorization",
                actionPerformed: {
                    retryAuthorization()
                }
        ))
        Thread.start {
            while (true) {
                if (scanAuthorization) {
                    authorized(adminPanel)
                }
                sleep(1000)
            }
        }
    }

    void logout() {
        authorizationRepository.deleteAll()
        refreshRepository.deleteAll()
    }

}
