package io.infinite.orbit.controllers

import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel
import io.infinite.orbit.entities.User
import io.infinite.orbit.repositories.UserRepository
import io.infinite.orbit.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@BlackBox(level = CarburetorLevel.METHOD)
@Slf4j
class UserController {

    @Autowired
    UserService userService

    @Autowired
    UserRepository userRepository

    @PostMapping(value = "/public/validateUserGuid/{guid}")
    @ResponseBody
    @CompileDynamic
    @CrossOrigin
    void validateUserGuid(@PathVariable("guid") String guid) {
        userService.validateGuid(guid)
    }

    @GetMapping(value = "/secured/user/phone/{phone}")
    @ResponseBody
    @CompileDynamic
    @CrossOrigin
    User findByPhone(@PathVariable("phone") String phone) {
        return userService.findByPhone(phone)
    }

    @PostMapping(value = "/secured/user/phone/{phone}")
    @ResponseBody
    @CompileDynamic
    @CrossOrigin
    User createUserByPhone(@PathVariable("phone") String phone) {
        return userService.createUserByPhone(phone)
    }

    @GetMapping(value = "/secured/user/email/{email}")
    @ResponseBody
    @CompileDynamic
    @CrossOrigin
    User findByEmail(@PathVariable("email") String email) {
        return userService.findByEmail(email)
    }

    @PostMapping(value = "/secured/user/email/{email}")
    @ResponseBody
    @CompileDynamic
    @CrossOrigin
    User createUserByEmail(@PathVariable("email") String email) {
        return userService.createUserByEmail(email)
    }

    @GetMapping(value = "/secured/admin/users")
    @ResponseBody
    @CompileDynamic
    @CrossOrigin
    Set<User> users() {
        return userRepository.findAll()
    }

}
