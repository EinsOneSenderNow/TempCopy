package ru.itis.javalab.controllers.configs;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static ru.itis.javalab.security.filters.JwtAuthenticationFilter.AUTHENTICATION_URL;
import static ru.itis.javalab.security.filters.JwtAuthenticationFilter.USERNAME_PARAMETER;

@Configuration
public class SecurityOpenApiConfig {
    private final String BEARER_AUTH = "bearerAuth";

    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI()
                .addSecurityItem(buildSecurity())
                .paths(buildAuthenticationPath())
                .components(buildComponents())
                .info(buildInfo());
    }

    private Paths buildAuthenticationPath() {
        return new Paths()
                .addPathItem(AUTHENTICATION_URL, buildAuthenticationPathItem());
    }

    private PathItem buildAuthenticationPathItem() {
        return new PathItem().post(
                new Operation()
                        .addTagsItem("Authentication")
                        .requestBody(buildAuthenticationRequestBody())
                        .responses(buildAuthenticationResponses()));
    }

    private RequestBody buildAuthenticationRequestBody() {
        return new RequestBody().content(
                new Content()
                        .addMediaType("application/x-www-form-urlencoded",
                                new MediaType()
                                        .schema(new Schema<>()
                                                .$ref("EmailAndPassword"))));
    }

    private ApiResponses buildAuthenticationResponses() {
        return new ApiResponses()
                .addApiResponse("200",
                        new ApiResponse()
                                .description("Refresh & access tokens")
                                .content(new Content()
                                        .addMediaType("application/json",
                                                new MediaType()
                                                        .schema(new Schema<>()
                                                                .$ref("Tokens")))))
                .addApiResponse("401",
                        new ApiResponse()
                                .description("Пользователь не авторизирован")
                                .content(new Content()
                                        .addMediaType("application/json",
                                                new MediaType()
                                                        .schema(new Schema<>()
                                                                .$ref("unauthorizedErrorDto")))));
    }

    private SecurityRequirement buildSecurity() {
        return new SecurityRequirement().addList(BEARER_AUTH);
    }

    private Info buildInfo() {
        return new Info().title("Help4Neighbour API");
    }

    private Components buildComponents() {

        Schema<?> emailAndPassword = new Schema<>()
                .type("object")
                .description("Почта и пароль пользователя")
                .addProperties(USERNAME_PARAMETER, new Schema<>().type("string"))
                .addProperties("password", new Schema<>().type("string"));

        Schema<?> tokens = new Schema<>()
                .type("object")
                .description("Access & Refresh tokens")
                .addProperties("accessToken", new Schema<>().type("string"))
                .addProperties("refreshToken", new Schema<>().type("string"));

        SecurityScheme securitySchema = new SecurityScheme()
                .name(BEARER_AUTH)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        Schema<?> errorSchema = new Schema<>()
                .type("object")
                .addProperties("timestamp", new Schema<>().type("integer").format("int32")
                        .example(1677759133))
                .addProperties("error", new Schema<>().type("string").example("Unauthorized"))
                .addProperties("status", new Schema<>().type("integer").example(401))
                .addProperties("path", new Schema<>().type("string").example("/auth/token"));
        errorSchema.setDescription("Пользователь не авторизирован");

        return new Components()
                .addSchemas("EmailAndPassword", emailAndPassword)
                .addSchemas("Tokens", tokens)
                .addSchemas("unauthorizedErrorDto", errorSchema)
                .addSecuritySchemes(BEARER_AUTH, securitySchema);
    }
}
