package io.infinite.orbit.controllers

import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel
import io.infinite.orbit.services.EncodingConversionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.multipart.MultipartFile

import javax.servlet.http.HttpServletResponse

@Controller
@BlackBox(level = CarburetorLevel.METHOD)
@Slf4j
class EncodingConversionController {

    @Autowired
    EncodingConversionService encodingConversionService

    @PostMapping(value = "/public/encodingConversion")
    @ResponseBody
    @CrossOrigin
    @CompileDynamic
    void convertEncoding(@RequestParam("file") MultipartFile multipartFile
                    , HttpServletResponse httpServletResponse
                    , @RequestParam(value = "fromCharsetName") String fromCharsetName
                    , @RequestParam(value = "toCharsetName") String toCharsetName
                    , @RequestParam(value = "width", required = false) Integer width
    ) {
        encodingConversionService.convertEncoding(
                multipartFile.inputStream,
                httpServletResponse.outputStream,
                fromCharsetName,
                toCharsetName,
                width
        )
    }

}
