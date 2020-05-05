package io.infinite.orbit.authentications


import io.infinite.ascend.common.entities.AuthenticationData
import io.infinite.ascend.common.entities.Authorization
import io.infinite.ascend.common.services.JwtService
import io.infinite.ascend.granting.client.authentication.AuthenticationPreparator
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

import java.time.Duration
import java.time.Instant

@BlackBox(level = CarburetorLevel.METHOD)
@Service
class ClientJwtPreparator implements AuthenticationPreparator {

    String ascendClientPublicKeyName

    String ascendClientPrivateKey

    @Override
    AuthenticationData prepareAuthentication() {
        Authorization selfIssuedAuthorization = new Authorization()
        Instant creationDate = Instant.now()
        selfIssuedAuthorization.creationDate = creationDate.toDate()
        selfIssuedAuthorization.setExpiryDate((creationDate + Duration.ofSeconds(60)).toDate())
        selfIssuedAuthorization.durationSeconds = 60
        JwtService jwtService = new JwtService()
        return new AuthenticationData(
                publicCredentials: ["ascendClientPublicKeyName": ascendClientPublicKeyName],
                privateCredentials: ["clientJwt": jwtService.authorization2jwt(
                        selfIssuedAuthorization,
                        jwtService.loadPrivateKeyFromHexString(ascendClientPrivateKey)
                )]
        )
    }

}
