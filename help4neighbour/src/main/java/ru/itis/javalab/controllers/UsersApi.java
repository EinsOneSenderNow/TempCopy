package ru.itis.javalab.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.javalab.dto.*;
import ru.itis.javalab.dto.users.SignUpDto;
import ru.itis.javalab.dto.users.UpdateUserDto;
import ru.itis.javalab.dto.users.UserDto;
import ru.itis.javalab.dto.users.UsersPage;

@Tags(value = {
        @Tag(name = "Users")
})
public interface UsersApi {
    @Operation(summary = "Получение пользователей на странице")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список пользователей на странице",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UsersPage.class))
                    }
            )
    })
    @GetMapping
    ResponseEntity<UsersPage> getAllUsers(@RequestParam("page") int page);

    @Operation(summary = "Добавление пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Добавленный пользователь",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class))
                    }
            )
    })
    @PostMapping
    ResponseEntity<UserDto> addUser(@RequestBody SignUpDto signUpDto);

    @Operation(summary = "Получение пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные о пользователе",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @GetMapping("/{user-id}")
    ResponseEntity<UserDto> getById(@Parameter(description = "id пользователя", example = "5")
                                    @PathVariable("user-id") Long userId);

    @Operation(summary = "Обновление пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Обновленный пользователь",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @PutMapping("/{user-id}")
    ResponseEntity<UserDto> updateById(@Parameter(description = "id пользователя", example = "5")
                                    @PathVariable("user-id") Long userId, @RequestBody UpdateUserDto updateUser);

    @Operation(summary = "Удаление пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Пользователь удален"),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @DeleteMapping("/{user-id}")
    ResponseEntity<?> deleteUser(@Parameter(description = "идентификатор пользователя", example = "8")
                                 @PathVariable("user-id") Long userId);

    @Operation(summary = "Публикация пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Опубликованный пользователь"),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @PutMapping("/{user-id}/confirm")
    ResponseEntity<UserDto> publishUser(@Parameter(description = "идентификатор пользователя", example = "8")
                                 @PathVariable("user-id") Long userId);
}
