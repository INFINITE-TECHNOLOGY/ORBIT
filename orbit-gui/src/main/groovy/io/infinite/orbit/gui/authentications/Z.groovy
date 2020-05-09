package io.infinite.orbit.gui.authentications

import groovy.json.JsonSlurper
import groovy.swing.SwingBuilder
import groovy.util.logging.Slf4j
import io.infinite.ascend.granting.client.authentication.AuthenticationPreparator
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel
import io.infinite.http.HttpException
import io.infinite.http.HttpRequest
import io.infinite.http.SenderDefaultHttps
import io.infinite.orbit.gui.OrbitGuiApp
import io.infinite.orbit.other.OrbitException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

import javax.annotation.PostConstruct
import javax.swing.*
import java.util.concurrent.LinkedBlockingQueue

@BlackBox(level = CarburetorLevel.METHOD)
@Slf4j
@Service
class Z implements AuthenticationPreparator {

    String ascendClientPrivateKey

    JPanel adminAuthenticationPanel = new JPanel()

    LinkedBlockingQueue userInputQueue = new LinkedBlockingQueue()

    JTextField phoneTextField = new JTextField(16)

    SwingBuilder swingBuilder = new SwingBuilder()

    JsonSlurper jsonSlurper = new JsonSlurper()

    SenderDefaultHttps senderDefaultHttps = new SenderDefaultHttps()

    @Value('${orbitUrl}')
    String orbitUrl

    @PostConstruct
    void init() {
        adminAuthenticationPanel.add(new JLabel("Please enter phone:"))
        adminAuthenticationPanel.add(phoneTextField)
        adminAuthenticationPanel.add(swingBuilder.button(
                text: "Confirm",
                actionPerformed: {
                    commence()
                }
        ))
        adminAuthenticationPanel.add(swingBuilder.button(
                text: "Cancel",
                actionPerformed: {
                    cancel()
                }
        ))
    }

    @Override
    void prepareAuthentication(Map<String, String> publicCredentials, Map<String, String> privateCredentials, Optional<String> prerequisiteJwt) {
        OrbitGuiApp.instance.showPanel(adminAuthenticationPanel)
        if (userInputQueue.take()) {
            def managedOtpHandle
            try {
                managedOtpHandle = jsonSlurper.parseText(senderDefaultHttps.expectStatus(
                        new HttpRequest(
                                url: "$orbitUrl/orbit/secured/sendOtpSms",
                                method: "POST",
                                headers: [
                                        "Content-Type" : "application/json",
                                        "Accept"       : "application/json",
                                        "Authorization": "Bearer ${prerequisiteJwt.get()}"
                                ],
                                body: """{
	"telephone": "+$telephone",
	"templateValues": {
		"action": "Registration"
	},
	"templateSelectionData": {
		"templateName": "OTP",
		"language": "eng"
	}
}"""
                        ),
                        200
                ).body)
            } catch (HttpException httpException) {
                log.warn("OTP sending exception", httpException)
                throw new OrbitException("Sorry, but there was a problem sending OTP to this phone.")
            }
        } else {
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
