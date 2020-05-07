package io.infinite.orbit.authentications

import groovy.swing.SwingBuilder
import io.infinite.ascend.common.entities.AuthenticationData
import io.infinite.ascend.common.exceptions.AscendException
import io.infinite.ascend.granting.client.authentication.ClientJwtPreparator
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel
import io.infinite.orbit.OrbitGuiApp
import org.springframework.stereotype.Service

import javax.annotation.PostConstruct
import javax.swing.*
import java.util.concurrent.LinkedBlockingQueue

@BlackBox(level = CarburetorLevel.METHOD)
@Service
class AdminPreparator extends ClientJwtPreparator {

    String ascendClientPublicKeyName

    String ascendClientPrivateKey

    JPanel adminAuthenticationPanel = new JPanel()

    LinkedBlockingQueue userInputQueue = new LinkedBlockingQueue()

    JTextField ascendClientPublicKeyNameTextField = new JTextField(16)

    SwingBuilder swingBuilder = new SwingBuilder()

    @PostConstruct
    void init() {
        adminAuthenticationPanel.add(new JLabel("Please enter Ascend Client Private Key Name:"))
        adminAuthenticationPanel.add(ascendClientPublicKeyNameTextField)
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
    AuthenticationData prepareAuthentication() {
        OrbitGuiApp.instance.showPanel(adminAuthenticationPanel)
        if (userInputQueue.take()) {
            return super.prepareAuthentication()
        } else {
            throw new AscendException("Authentication cancelled as per user request")
        }
    }

    void commence() {
        ascendClientPublicKeyName = ascendClientPublicKeyNameTextField.text
        userInputQueue.put(true)
    }

    void cancel() {
        userInputQueue.put(false)
    }

}
