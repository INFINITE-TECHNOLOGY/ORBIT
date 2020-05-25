package io.infinite.orbit.services


import io.infinite.http.HttpRequest
import io.infinite.http.HttpResponse
import io.infinite.http.SenderDefaultHttps
import org.apache.commons.codec.binary.Base64
import org.springframework.beans.factory.annotation.Value

import java.security.KeyFactory
import java.security.PrivateKey
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec

class CrmServiceBase {

    SenderDefaultHttps senderDefaultHttps = new SenderDefaultHttps()

    @Value('${crmUrl}')
    String crmUrl

    @Value('${crmPrivateKey}')
    String crmPrivateKey

    PrivateKey privateKey = getPrivateKey()

    HttpResponse crmRequest(String body) {
        return senderDefaultHttps.expectStatus(
                new HttpRequest(
                        url: "$crmUrl/external/extended",
                        method: "POST",
                        headers: [
                                "Content-Type": "text/xml;charset=UTF-8",
                                "Accept"      : "text/xml;charset=UTF-8",
                                "X-Signature" : sign(body)
                        ],
                        body: body
                ), 200
        )
    }

    String sign(String message) {
        Signature signature = Signature.getInstance("SHA1withRSA")
        signature.initSign(privateKey)
        signature.update(message.getBytes("UTF-8"))
        return new String(Base64.encodeBase64(signature.sign()), "UTF-8")
    }

    PrivateKey getPrivateKey() throws Exception {
        if (![null, ""].contains(System.getenv("crmPrivateKey"))) {
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(System.getenv("crmPrivateKey").decodeBase64())
            KeyFactory kf = KeyFactory.getInstance("RSA")
            return kf.generatePrivate(spec)
        } else {
            return null
        }
    }

}
