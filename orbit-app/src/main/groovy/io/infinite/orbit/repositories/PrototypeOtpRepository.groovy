package io.infinite.orbit.repositories

import io.infinite.orbit.entities.PrototypeOtp
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(exported = false)
interface PrototypeOtpRepository extends JpaRepository<PrototypeOtp, Long> {

    PrototypeOtp findByNamespace(String namespace)

}
