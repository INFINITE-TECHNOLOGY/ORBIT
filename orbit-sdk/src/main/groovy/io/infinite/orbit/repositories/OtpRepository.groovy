package io.infinite.orbit.repositories


import io.infinite.orbit.entities.Otp
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(exported = false)
interface OtpRepository extends JpaRepository<Otp, Long> {

    Optional<Otp> findByGuid(UUID guid)

}
