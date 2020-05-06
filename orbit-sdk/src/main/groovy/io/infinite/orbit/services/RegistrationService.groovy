package io.infinite.orbit.services

import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel
import io.infinite.orbit.entities.Registration
import io.infinite.orbit.repositories.RegistrationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
@BlackBox(level = CarburetorLevel.METHOD)
@Slf4j
class RegistrationService {

    @Autowired
    RegistrationRepository registrationRepository

    Registration createRegistration(String namespace, String phone) {
        try {
            return registrationRepository.saveAndFlush(new Registration(
                    namespace: namespace,
                    phone: phone
            ))
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Registration error", exception)
        }
    }

    Registration findByNamespaceAndPhone(String namespace, String phone) {
        try {
            Set<Registration> registrations = registrationRepository.findByNamespaceAndPhone(namespace, phone)
            if (registrations.empty) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND)
            }
            if (registrations.size() > 1) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Duplicate phone")
            }
            return registrations.first()
        } catch (ResponseStatusException responseStatusException) {
            throw responseStatusException
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Registration search error", exception)
        }
    }

    void validateGuid(String guid) {
        try {
            Optional<Registration> registrationOptional = registrationRepository.findByGuid(UUID.fromString(guid))
            if (!registrationOptional.present) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND)
            }
        } catch (ResponseStatusException responseStatusException) {
            throw responseStatusException
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Registration search error", exception)
        }
    }

    void validateAdminGuid(String guid) {
        try {
            Optional<Registration> registrationOptional = registrationRepository.findByGuid(UUID.fromString(guid))
            if (!registrationOptional.present) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND)
            }
            if (!registrationOptional.get().isAdmin) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST)
            }
        } catch (ResponseStatusException responseStatusException) {
            throw responseStatusException
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Admin validation error", exception)
        }
    }

}
