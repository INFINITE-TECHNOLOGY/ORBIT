package io.infinite.orbit.repositories


import io.infinite.orbit.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(exported = false)
interface UserRepository extends JpaRepository<User, Long> {

    Set<User> findByPhone(String phone)

    Optional<User> findByGuid(UUID guid)

}
