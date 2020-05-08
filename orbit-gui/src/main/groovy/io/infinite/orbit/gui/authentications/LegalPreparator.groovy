package io.infinite.orbit.gui.authentications

import groovy.json.JsonSlurper
import groovy.swing.SwingBuilder
import groovy.util.logging.Slf4j
import io.infinite.ascend.granting.client.authentication.AuthenticationPreparator
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel
import io.infinite.orbit.gui.OrbitGuiApp
import io.infinite.orbit.other.OrbitException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

import javax.annotation.PostConstruct
import javax.swing.*
import java.awt.Dimension
import java.util.concurrent.LinkedBlockingQueue

@BlackBox(level = CarburetorLevel.METHOD)
@Slf4j
@Service
class LegalPreparator implements AuthenticationPreparator {

    @Value('${orbitUrl}')
    String orbitUrl

    String ascendClientPrivateKey

    Box box = new Box(BoxLayout.Y_AXIS)

    JPanel authorizationPanel = new JPanel().add(box).parent as JPanel

    LinkedBlockingQueue userInputQueue = new LinkedBlockingQueue()

    JEditorPane privacyPolicyTextArea

    SwingBuilder swingBuilder = new SwingBuilder()

    JsonSlurper jsonSlurper = new JsonSlurper()

    @PostConstruct
    void init() {
        box.add(new JLabel("Please review and accept the Privacy Policy:"))
        privacyPolicyTextArea = new JEditorPane("$orbitUrl/orbit/public/PrivacyPolicy.html")
        privacyPolicyTextArea.preferredSize = new Dimension(800, 600)
        box.add(privacyPolicyTextArea)
        box.add(swingBuilder.button(
                text: "Confirm",
                actionPerformed: {
                    commence()
                }
        ))
        box.add(swingBuilder.button(
                text: "Cancel",
                actionPerformed: {
                    cancel()
                }
        ))
    }

    @Override
    void prepareAuthentication(Map<String, String> publicCredentials, Map<String, String> privateCredentials, Optional<String> prerequisiteJwt) {
        OrbitGuiApp.instance.showPanel(authorizationPanel)
        if (!userInputQueue.take()) {
            throw new OrbitException("Authentication cancelled as per user request")
        }
    }

    void commence() {
        userInputQueue.put(true)
    }

    void cancel() {
        userInputQueue.put(false)
    }

}
