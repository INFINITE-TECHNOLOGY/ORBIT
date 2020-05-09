package io.infinite.orbit.gui.authentications

import groovy.json.JsonSlurper
import groovy.swing.SwingBuilder
import groovy.util.logging.Slf4j
import io.infinite.ascend.granting.client.authentication.AuthenticationPreparator
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel
import io.infinite.http.HttpRequest
import io.infinite.http.HttpResponse
import io.infinite.http.SenderDefaultHttps
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
class AdminPreparator implements AuthenticationPreparator {

    @Value('${orbitUrl}')
    String orbitUrl

    Box box = new Box(BoxLayout.Y_AXIS)

    JLabel messageLabel = box.add(new JLabel("Pre-authenticating...")) as JLabel

    JPanel authorizationPanel = new JPanel().add(box).parent as JPanel

    SenderDefaultHttps senderDefaultHttps = new SenderDefaultHttps()

    JsonSlurper jsonSlurper = new JsonSlurper()

    @Override
    void prepareAuthentication(Map<String, String> publicCredentials, Map<String, String> privateCredentials, Optional<String> prerequisiteJwt) {
        OrbitGuiApp.instance.showPanel(authorizationPanel)
        String url
        if (publicCredentials.containsKey("phone")) {
            url = "$orbitUrl/orbit/secured/admin/search/findByPhone?phone=${publicCredentials.get("phone")}"
        } else if (publicCredentials.containsKey("email")) {
            url = "$orbitUrl/orbit/secured/admin/search/findByEmail?email=${publicCredentials.get("email")}"
        } else {
            throw new OrbitException("Missing required public credentials.")
        }
        HttpResponse httpResponse = senderDefaultHttps.sendHttpMessage(
                new HttpRequest(
                        url: url,
                        method: "GET",
                        headers: [
                                "Content-Type" : "application/json",
                                "Accept"       : "application/json",
                                "Authorization": "Bearer ${prerequisiteJwt.get()}"
                        ]
                )
        )
        String adminGuid
        if (httpResponse.status == 404) {
            throw new OrbitException("Pre-authentication failed. This phone is not registered for Admin authentication.")
        } else if (httpResponse.status == 200) {
            messageLabel.text = "Pre-authentication successful. Requesting authorization..."
            adminGuid = jsonSlurper.parseText(httpResponse.body).guid
        } else {
            throw new OrbitException("Sorry, there was a problem during pre-authentication.")
        }
        publicCredentials.put("adminGuid", adminGuid)
    }

}
