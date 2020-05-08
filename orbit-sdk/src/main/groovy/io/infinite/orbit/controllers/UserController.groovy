package io.infinite.orbit.controllers

import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel
import io.infinite.orbit.entities.User
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

    @PostMapping(value = "/public/validateUserGuid/{guid}")
    @ResponseBody
    @CompileDynamic
    @CrossOrigin
    void validateUserGuid(@PathVariable("guid") String guid) {
        userService.validateGuid(guid)
    }

    @GetMapping(value = "/secured/user/{phone}")
    @ResponseBody
    @CompileDynamic
    @CrossOrigin
    User findByPhone(@PathVariable("phone") String phone) {
        return userService.findByPhone(phone)
    }

    @PostMapping(value = "/secured/user/{phone}")
    @ResponseBody
    @CompileDynamic
    @CrossOrigin
    User createUser(@PathVariable("phone") String phone) {
        return userService.createUser(phone)
    }

}
