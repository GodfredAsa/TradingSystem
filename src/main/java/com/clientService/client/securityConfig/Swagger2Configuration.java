package com.clientService.client.securityConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Import({SpringDataRestConfiguration.class})
public class Swagger2Configuration implements WebMvcConfigurer {
    private final String baseUrl;

    public Swagger2Configuration(
            @Value("${springfox.documentation.swagger-ui.base-url:}") String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String baseUrl = StringUtils.trimTrailingCharacter(this.baseUrl, '/');
        registry.
                addResourceHandler(baseUrl + "/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                .resourceChain(false);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController(baseUrl + "/swagger-ui/")
                .setViewName("forward:" + baseUrl + "/swagger-ui/index.html");
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .host(baseUrl)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.clientService.clientService.client.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiEndPointsInfo());

    }

    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder()
                .title("client order service.")
                .description("Trading system for client and order service")
                .contact(new Contact("Group06", "", "turntabl.io"))
                .license("Apache 2.0")
                .licenseUrl("")
                .version("0.0.1")
                .build();
    }
}