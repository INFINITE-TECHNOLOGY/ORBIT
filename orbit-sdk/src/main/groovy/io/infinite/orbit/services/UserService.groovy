package io.infinite.orbit.services

import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel
import io.infinite.orbit.entities.User
import io.infinite.orbit.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
@BlackBox(level = CarburetorLevel.METHOD)
@Slf4j
class UserService {

    @Autowired
    UserRepository userRepository

    User createUser(String namespace, String phone) {
        try {
            return userRepository.saveAndFlush(new User(
                    namespace: namespace,
                    phone: phone
            ))
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User error", exception)
        }
    }

    User findByNamespaceAndPhone(String namespace, String phone) {
        try {
            Set<User> users = userRepository.findByNamespaceAndPhone(namespace, phone)
            if (users.empty) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND)
            }
            if (users.size() > 1) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Duplicate phone")
            }
            return users.first()
        } catch (ResponseStatusException responseStatusException) {
            throw responseStatusException
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User search error", exception)
        }
    }

    void validateGuid(String guid) {
        try {
            Optional<User> userOptional = userRepository.findByGuid(UUID.fromString(guid))
            if (!userOptional.present) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND)
            }
        } catch (ResponseStatusException responseStatusException) {
            throw responseStatusException
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User search error", exception)
        }
    }

}
