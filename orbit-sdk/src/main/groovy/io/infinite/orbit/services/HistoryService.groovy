package io.infinite.orbit.services

import com.fasterxml.jackson.databind.ObjectMapper
import groovy.util.logging.Slf4j
import io.infinite.ascend.common.services.JwtService
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel
import io.infinite.http.HttpRequest
import io.infinite.http.HttpResponse
import io.infinite.http.SenderDefaultHttps
import io.infinite.orbit.model.HistoryRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller

import java.security.Signature

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

    JwtService jwtService = new JwtService()

    String sign(String message) {
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initSign(jwtService.loadPrivateKeyFromHexString(crmPrivateKey))
        signature.update(message.getBytes("UTF-8"));
        return signature.sign().encodeBase64()
    }

    Set<HistoryRecord> getHistory(String userGuid) {
        HttpResponse httpResponse = senderDefaultHttps.expectStatus(
                new HttpRequest(
                        url: "$crmUrl/external/extended",
                        method: "POST",
                        headers: [
                                "Content-Type": "application/xml",
                                "Accept"      : "application/xml",
                                "X-Signature" : sign(body)
                        ],
                        body: body
                ), 200
        )
        return []
    }

    String body =  """<request point="12345">
<reconciliation begin="2007-10-12T12:00:00+0300"
end="2007-10-13T12:00:00+0300"
payments="1"
offset="1000"/>
</request>"""

}
