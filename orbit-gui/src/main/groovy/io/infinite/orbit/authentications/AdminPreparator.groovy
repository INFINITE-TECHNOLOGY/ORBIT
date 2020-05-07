package io.infinite.orbit.authentications

import io.infinite.ascend.common.entities.AuthenticationData
import io.infinite.ascend.common.exceptions.AscendException
import io.infinite.ascend.granting.client.authentication.ClientJwtPreparator
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel
import io.infinite.orbit.OrbitGuiApp
import org.springframework.stereotype.Service

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

    @Override
    AuthenticationData prepareAuthentication() {
        OrbitGuiApp orbitGuiApp = OrbitGuiApp.instance
        adminAuthenticationPanel.add(new JLabel("Please enter Ascend Client Private Key Name:"))
        adminAuthenticationPanel.add(ascendClientPublicKeyNameTextField)
        adminAuthenticationPanel.add(orbitGuiApp.swing.button(
                text: "Confirm",
                actionPerformed: {
                    commence()
                }
        ))
        adminAuthenticationPanel.add(orbitGuiApp.swing.button(
                text: "Cancel",
                actionPerformed: {
                    cancel()
                }
        ))
        orbitGuiApp.showPanel(adminAuthenticationPanel)
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
