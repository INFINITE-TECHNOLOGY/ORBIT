package io.infinite.orbit.controllers

import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel
import io.infinite.orbit.entities.Admin
import io.infinite.orbit.services.AdminService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@BlackBox(level = CarburetorLevel.METHOD)
@Slf4j
class AdminController {

    @Autowired
    AdminService adminService

    @PostMapping(value = "/public/validateAdminGuid/{guid}")
    @ResponseBody
    @CompileDynamic
    @CrossOrigin
    void validateAdminGuid(@PathVariable("guid") String guid) {
        adminService.validateGuid(guid)
    }

    @GetMapping(value = "/secured/admin/search/findByPhone")
    @ResponseBody
    @CompileDynamic
    @CrossOrigin
    Admin findByPhone(@RequestParam("phone") String phone) {
        return adminService.findByPhone(phone)
    }

    @GetMapping(value = "/secured/admin/search/findByEmail")
    @ResponseBody
    @CompileDynamic
    @CrossOrigin
    Admin findByEmail(@RequestParam("email") String email) {
        return adminService.findByEmail(email)
    }

    @PostMapping(value = "/secured/admin")
    @ResponseBody
    @CompileDynamic
    @CrossOrigin
    Admin createAdmin(@RequestBody Admin admin) {
        return adminService.createAdmin(admin)
    }

}
