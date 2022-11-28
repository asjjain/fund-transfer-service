package com.fundtransfer.fundtransferservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
public class SwaggerConfig {
    @Value("${swagger.group.name}")
    private String groupName;
    @Value("${swagger.paths.regex}")
    private String pathsRegex;
    @Value("${swagger.title}")
    private String title;
    @Value("${swagger.description}")
    private String description;
    @Value("${swagger.terms.service.url}")
    private String termsURL;
    @Value("${swagger.contact}")
    private String contactName;
    private Contact CONTACT = new Contact(contactName, "",
            "ashish.jain1911@gmail.com");
    @Value("${swagger.version}")
    private String version;

   @Bean
    public Docket fundTransferApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.fundtransfer.fundtransferservice.controller"))
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/");

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .termsOfServiceUrl(termsURL)
                .contact(CONTACT)
                .version(version)
                .build();
    }



}