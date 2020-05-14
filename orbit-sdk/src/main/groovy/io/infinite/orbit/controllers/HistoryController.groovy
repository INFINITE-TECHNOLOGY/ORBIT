package io.infinite.orbit.controllers

import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel
import io.infinite.orbit.model.HistoryRecord
import io.infinite.orbit.services.HistoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@BlackBox(level = CarburetorLevel.METHOD)
@Slf4j
class HistoryController {

    @Autowired
    HistoryService historyService

    @GetMapping(value = "/secured/history/{phone}")
    @ResponseBody
    @CompileDynamic
    @CrossOrigin
    Set<HistoryRecord> getHistory(
            @PathVariable("phone") String phone,
            @RequestParam("tranCount") Optional<String> tranCount
    ) {
        return historyService.getHistory(phone, tranCount)
    }

}
