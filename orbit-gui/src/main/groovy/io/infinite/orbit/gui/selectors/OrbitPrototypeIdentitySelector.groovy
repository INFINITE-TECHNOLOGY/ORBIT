package io.infinite.orbit.gui.selectors

import groovy.json.JsonSlurper
import groovy.swing.SwingBuilder
import io.infinite.ascend.granting.client.services.selectors.PrototypeIdentitySelector
import io.infinite.ascend.granting.configuration.entities.PrototypeIdentity
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel
import io.infinite.orbit.gui.OrbitGuiApp
import io.infinite.orbit.other.OrbitException
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

import javax.annotation.PostConstruct
import javax.swing.*
import java.util.concurrent.LinkedBlockingQueue

@Service
@Primary
@BlackBox(level = CarburetorLevel.METHOD)
class OrbitPrototypeIdentitySelector implements PrototypeIdentitySelector {

    @Value('${orbitUrl}')
    String orbitUrl

    String ascendClientPrivateKey

    Box box

    JPanel panel

    LinkedBlockingQueue userInputQueue = new LinkedBlockingQueue()

    JEditorPane privacyPolicyTextArea

    JsonSlurper jsonSlurper = new JsonSlurper()

    PrototypeIdentity selectAny(Set<PrototypeIdentity> prototypeIdentities) {
        SwingBuilder swingBuilder = new SwingBuilder()
        if (prototypeIdentities.size() == 1) {
            return prototypeIdentities.first()
        }
        Box box = new Box(BoxLayout.Y_AXIS)
        box.add(new JLabel("Proceed as:"))
        JPanel panel = new JPanel().add(box).parent as JPanel
        prototypeIdentities.each {
            String name = it.name
            box.add(swingBuilder.button(
                    text: name,
                    actionPerformed: {
                        choose(name)
                    }
            ))
        }
        box.add(swingBuilder.button(
                text: "Cancel",
                actionPerformed: {
                    cancel()
                }
        ))
        OrbitGuiApp.instance.showPanel(panel)
        String result = userInputQueue.take()
        if (result == "Cancel") {
            throw new OrbitException("Action cancelled as per user request")
        } else {
            return prototypeIdentities.find {
                it.name = result
            }
        }
    }

    void choose(String name) {
        userInputQueue.put(name)
    }

    void cancel() {
        userInputQueue.put("Cancel")
    }

    @Override
    PrototypeIdentity select(Set<PrototypeIdentity> prototypeIdentities) {
        return selectAny(prototypeIdentities)
    }

    @Override
    PrototypeIdentity selectPrerequisite(Set<PrototypeIdentity> prototypeIdentities) {
        return selectAny(prototypeIdentities)
    }
}
