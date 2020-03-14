package io.infinite.orbit.configurations.mvc


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import io.infinite.blackbox.BlackBox
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

/**
 * Taken from: https://blog.jdriven.com/2016/11/handling-yaml-format-rest-spring-boot/
 */
@Configuration
@BlackBox
class ObjectMapperConfiguration {

    //This is our default JSON ObjectMapper. Add @Primary to inject is as default bean.
    @Bean
    @Primary
    ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper()
        //Enable or disable features
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT)
        return objectMapper
    }

    @Bean
    ObjectMapper yamlObjectMapper() {
        //Enable or disable features
        YAMLFactory yamlFactory = new YAMLFactory()
        yamlFactory.enable(YAMLGenerator.Feature.MINIMIZE_QUOTES)
        yamlFactory.disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
        yamlFactory.disable(YAMLGenerator.Feature.SPLIT_LINES)
        yamlFactory.enable(YAMLGenerator.Feature.INDENT_ARRAYS)
        ObjectMapper yamlObjectMapper = new ObjectMapper(yamlFactory)
        return yamlObjectMapper
    }
}