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

import javax.swing.*
import java.util.concurrent.LinkedBlockingQueue

@BlackBox(level = CarburetorLevel.METHOD)
@Slf4j
@Service
class EmailOtpPreparator implements AuthenticationPreparator {

    @Value('${orbitUrl}')
    String orbitUrl

    LinkedBlockingQueue userInputQueue = new LinkedBlockingQueue()

    SwingBuilder swingBuilder = new SwingBuilder()

    JsonSlurper jsonSlurper = new JsonSlurper()

    JTextField emailField = new JTextField(32)

    JTextField otpField = new JTextField(32)

    JLabel warningLabel = new JLabel("Incorrect email format")

    JLabel incorrectOtpLabel = new JLabel("Incorrect OTP")

    SenderDefaultHttps senderDefaultHttps = new SenderDefaultHttps()

    @Override
    void prepareAuthentication(Map<String, String> publicCredentials, Map<String, String> privateCredentials, Optional<String> prerequisiteJwt) {
        Box box = new Box(BoxLayout.Y_AXIS)
        JPanel panel = new JPanel().add(box).parent as JPanel
        box.add(new JLabel("Please enter your registered email:"))
        box.add(emailField)
        warningLabel.visible = false
        box.add(warningLabel)
        box.add(swingBuilder.button(
                text: "Confirm",
                actionPerformed: {
                    emailEntered()
                }
        ))
        box.add(swingBuilder.button(
                text: "Cancel",
                actionPerformed: {
                    cancel()
                }
        ))
        OrbitGuiApp.instance.showPanel(panel)
        if (!userInputQueue.take()) {
            throw new OrbitException("Authentication cancelled as per user request")
        }
        def managedOtpHandle
        try {
            managedOtpHandle = jsonSlurper.parseText(senderDefaultHttps.expectStatus(
                    new HttpRequest(
                            url: "$orbitUrl/orbit/secured/sendOtpEmail",
                            method: "POST",
                            headers: [
                                    "Content-Type" : "application/json",
                                    "Accept"       : "application/json",
                                    "Authorization": "Bearer ${prerequisiteJwt.get()}"
                            ],
                            body: """{
	"to": "+${emailField.text}",
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
            throw new OrbitException("Sorry, but there was a problem sending OTP to this email.")
        }
        box.removeAll()
        box.add(new JLabel("Please enter OTP:"))
        otpField.text = ""
        box.add(otpField)
        incorrectOtpLabel.visible = false
        box.add(incorrectOtpLabel)
        OrbitGuiApp.instance.showPanel(panel)
        box.add(swingBuilder.button(
                text: "Confirm",
                actionPerformed: {
                    otpEntered(managedOtpHandle.guid as String)
                }
        ))
        box.add(swingBuilder.button(
                text: "Cancel",
                actionPerformed: {
                    cancel()
                }
        ))
        OrbitGuiApp.instance.showPanel(panel)
        if (!userInputQueue.take()) {
            throw new OrbitException("Authentication cancelled as per user request")
        }
        publicCredentials.put("email", emailField.text)
        publicCredentials.put("otpGuid", managedOtpHandle.guid as String)
        privateCredentials.put("otp", otpField.text)
    }

    void otpEntered(String otpGuid) {
        if (validateOtp(otpGuid, otpField.text) == 200) {
            userInputQueue.put(true)
        } else {
            incorrectOtpLabel.visible = true
        }
    }

    void emailEntered() {
        if (emailField.text.matches("""(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])""")) {
            userInputQueue.put(true)
        } else {
            warningLabel.visible = true
        }
    }

    void cancel() {
        userInputQueue.put(false)
    }

    Integer validateOtp(String guid, String userOtp) {
        return senderDefaultHttps.sendHttpMessage(
                new HttpRequest(
                        url: "https://orbit-secured.herokuapp.com/orbit/public/validateOtp",
                        method: "POST",
                        headers: [
                                "Content-Type": "application/json",
                                "Accept"      : "application/json"
                        ],
                        body: """{
	"guid": "$guid",
	"otp": "$userOtp"
}"""
                )
        ).status
    }

}
