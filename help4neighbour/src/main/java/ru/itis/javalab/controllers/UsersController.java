package ru.itis.javalab.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.javalab.dto.users.SignUpDto;
import ru.itis.javalab.dto.users.UpdateUserDto;
import ru.itis.javalab.dto.users.UserDto;
import ru.itis.javalab.dto.users.UsersPage;
import ru.itis.javalab.services.UserService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UsersController implements UsersApi{
    private final UserService userService;

    @Override
    public ResponseEntity<UsersPage> getAllUsers(int page) {
        return ResponseEntity
                .ok(userService.getAllUsers(page));
    }

    @Override
    public ResponseEntity<UserDto> addUser(@Valid SignUpDto signUpDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.addUser(signUpDto));
    }

    @Override
    public ResponseEntity<UserDto> getById(Long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @Override
    public ResponseEntity<UserDto> updateById(Long userId, @Valid UpdateUserDto updateUser) {
        return ResponseEntity.accepted()
                .body(userService.updateUser(userId, updateUser));
    }

    @Override
    public ResponseEntity<?> deleteUser(Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<UserDto> publishUser(Long userId) {
        return ResponseEntity.accepted()
                .body(userService.publishUser(userId));
    }
}
