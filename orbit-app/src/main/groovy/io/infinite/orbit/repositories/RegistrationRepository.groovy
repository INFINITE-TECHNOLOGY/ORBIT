package io.infinite.orbit.repositories


import io.infinite.orbit.entities.Registration
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(exported = false)
interface RegistrationRepository extends JpaRepository<Registration, Long> {

    Set<Registration> findByNamespaceAndPhone(String namespace, String phone)

}
