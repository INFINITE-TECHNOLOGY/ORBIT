package io.infinite.orbit.services

import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel
import io.infinite.orbit.entities.Admin
import io.infinite.orbit.repositories.AdminRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
@BlackBox(level = CarburetorLevel.METHOD)
@Slf4j
class AdminService {

    @Autowired
    AdminRepository adminRepository

    Admin createAdmin(Admin admin) {
        try {
            return adminRepository.saveAndFlush(admin)
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Admin error", exception)
        }
    }

    Admin findByPhone(String phone) {
        try {
            Set<Admin> admins = adminRepository.findByPhone(phone)
            if (admins.empty) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND)
            }
            if (admins.size() > 1) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Duplicate phone")
            }
            return admins.first()
        } catch (ResponseStatusException responseStatusException) {
            throw responseStatusException
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Admin search error", exception)
        }
    }

    Admin findByEmail(String email) {
        try {
            Set<Admin> admins = adminRepository.findByEmail(email)
            if (admins.empty) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND)
            }
            if (admins.size() > 1) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Duplicate email")
            }
            return admins.first()
        } catch (ResponseStatusException responseStatusException) {
            throw responseStatusException
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Admin search error", exception)
        }
    }

    void validateGuid(String guid) {
        try {
            Optional<Admin> adminOptional = adminRepository.findByGuid(UUID.fromString(guid))
            if (!adminOptional.present) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND)
            }
        } catch (ResponseStatusException responseStatusException) {
            throw responseStatusException
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Admin search error", exception)
        }
    }

}
