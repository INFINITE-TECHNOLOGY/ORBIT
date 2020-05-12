package io.infinite.orbit.services

import com.fasterxml.jackson.databind.ObjectMapper
import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel
import io.infinite.http.HttpRequest
import io.infinite.http.HttpResponse
import io.infinite.http.SenderDefaultHttps
import io.infinite.orbit.model.HistoryRecord
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller

import java.security.KeyFactory
import java.security.PrivateKey
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec

import org.apache.commons.codec.binary.Base64

@Controller
@BlackBox(level = CarburetorLevel.METHOD)
@Slf4j
class HistoryService {

    ObjectMapper objectMapper = new ObjectMapper()

    SenderDefaultHttps senderDefaultHttps = new SenderDefaultHttps()

    @Value('${crmUrl}')
    String crmUrl

    @Value('${crmPrivateKey}')
    String crmPrivateKey

    PrivateKey privateKey = getPrivateKey()

    String sign(String message) {
        Signature signature = Signature.getInstance("SHA1withRSA")
        signature.initSign(privateKey)
        signature.update(message.getBytes("UTF-8"))
        return new String(Base64.encodeBase64(signature.sign()),"UTF-8")
    }

    PrivateKey getPrivateKey() throws Exception {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(System.getenv("crmPrivateKey").decodeBase64())
        KeyFactory kf = KeyFactory.getInstance("RSA")
        return kf.generatePrivate(spec)
    }

    Set<HistoryRecord> getHistory(String userGuid) {
        HttpResponse httpResponse = senderDefaultHttps.expectStatus(
                new HttpRequest(
                        url: "$crmUrl/external/extended",
                        method: "POST",
                        headers: [
                                "Content-Type": "application/xml",
                                "Accept"      : "application/xml",
                                "X-Signature" : sign("x")
                        ],
                        body: body
                ), 200
        )
        return []
    }

    String body = "w"

}
