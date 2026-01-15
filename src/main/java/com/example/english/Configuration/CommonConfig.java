package com.example.english.Configuration;

import com.cloudinary.Cloudinary;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class CommonConfig implements WebMvcConfigurer{
    @Bean
    public OpenAPI openAPI(
            @Value("${open.api.service.title}") String title,
            @Value("${open.api.service.version}") String version,
            @Value("${open.api.service.description}") String description,
            @Value("${open.api.service.serverUrl}") String serverUrl,
            @Value("${open.api.service.serverName}") String serverName) {

        return new OpenAPI()
                // Builder style
                .info(new Info()
                        .title(title)
                        .description(description)
                        .version(version)
                        .license(new License().name("Apache 2.0").url("https://springdoc.org")))
                .servers(List.of(new Server().url(serverUrl).description(serverName)))
                .components(new Components().addSecuritySchemes("bearerAuth",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .security(List.of(new SecurityRequirement().addList("bearerAuth")));

    }
    @Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")

                        .allowedOrigins("http://localhost:8080","http://localhost:5173","https://unrealistic-elton-denunciable.ngrok-free.dev")
                        .allowedMethods("PATCH","GET","POST","PUT","DELETE","OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
    @Value("${cloudinary.cloud_name}")
    String cloudName;
    @Value("${cloudinary.api_key}")
    String apiKey;
    @Value("${cloudinary.api_secret}")
    String apiSecret;
    @Bean
    public Cloudinary getCloudinary(){
        final Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        return new Cloudinary(config);
    }
}
