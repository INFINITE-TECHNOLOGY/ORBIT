package io.infinite.orbit.repositories


import io.infinite.orbit.entities.Admin
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(exported = false)
interface AdminRepository extends JpaRepository<Admin, Long> {

    Set<Admin> findByNamespaceAndPhone(String namespace, String phone)

    Set<Admin> findByNamespaceAndEmail(String namespace, String email)

    Optional<Admin> findByGuid(UUID guid)

}
