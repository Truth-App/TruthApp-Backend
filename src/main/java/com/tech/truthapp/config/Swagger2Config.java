package com.tech.truthapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config {
	
	@Bean
    public Docket api() {
		 return new Docket(DocumentationType.SWAGGER_2)
	                .select()
	                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
	                .paths(PathSelectors.any())
	                .build()
	                .apiInfo(metaData());
	}
	
    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title("Truth APP REST API")
                .description("This is Sample API")
                .version("0.0.1")
                .license("License Details")
                .licenseUrl("License URL")
                .contact(new Contact("Prem Kumar", "https://mailtruthapp.org", "mailtruthapp@gmail.com"))
                .build();
    }
}
