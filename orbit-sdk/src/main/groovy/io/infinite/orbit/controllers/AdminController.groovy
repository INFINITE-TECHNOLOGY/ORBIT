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

    @GetMapping(value = "/{namespace}/admin/search/findByPhone")
    @ResponseBody
    @CompileDynamic
    @CrossOrigin
    Admin findByNamespaceAndPhone(@PathVariable("namespace") String namespace, @RequestParam("phone") String phone) {
        return adminService.findByNamespaceAndPhone(namespace, phone)
    }

    @GetMapping(value = "/{namespace}/admin/search/findByEmail")
    @ResponseBody
    @CompileDynamic
    @CrossOrigin
    Admin findByNamespaceAndEmail(@PathVariable("namespace") String namespace, @RequestParam("email") String email) {
        return adminService.findByNamespaceAndEmail(namespace, email)
    }

    @PostMapping(value = "/{namespace}/admin")
    @ResponseBody
    @CompileDynamic
    @CrossOrigin
    Admin createAdmin(@PathVariable("namespace") String namespace, @RequestBody Admin admin) {
        return adminService.createAdmin(namespace, admin)
    }

}
