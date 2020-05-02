package io.infinite.orbit.configurations.mvc

import com.fasterxml.jackson.databind.ObjectMapper
import io.infinite.blackbox.BlackBox
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.filter.CommonsRequestLoggingFilter
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * Taken from: https://blog.jdriven.com/2016/11/handling-yaml-format-rest-spring-boot/
 */
@Configuration
@BlackBox
class WebMvcConfiguration implements WebMvcConfigurer {

    private static final MediaType MEDIA_TYPE_YAML = MediaType.valueOf("text/yaml")

    @Autowired
    @Qualifier("yamlObjectMapper")
    private ObjectMapper yamlObjectMapper

    @Override
    void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer
                .favorPathExtension(false)
                .favorParameter(true)
                .ignoreAcceptHeader(false)
                .mediaType(MediaType.APPLICATION_JSON.subtype, MediaType.APPLICATION_JSON)
                .mediaType(MEDIA_TYPE_YAML.subtype, MEDIA_TYPE_YAML)
    }

    @Override
    void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter yamlConverter =
                new MappingJackson2HttpMessageConverter(yamlObjectMapper)
        yamlConverter.supportedMediaTypes = [MEDIA_TYPE_YAML]
        converters.add(yamlConverter)
    }

    @Bean
    /**
     * Taken from: https://stackoverflow.com/a/50922329/6784237
     */
    CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter()
        filter.includeQueryString = true
        filter.includePayload = true
        filter.maxPayloadLength = 100000
        filter.includeHeaders = false
        filter.afterMessagePrefix = "REQUEST DATA : "
        return filter
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/public/")
    }

}