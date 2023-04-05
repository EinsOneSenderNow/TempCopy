package ru.itis.javalab.services;

import ru.itis.javalab.dto.users.SignUpDto;
import ru.itis.javalab.dto.users.UpdateUserDto;
import ru.itis.javalab.dto.users.UserDto;
import ru.itis.javalab.dto.users.UsersPage;

public interface UserService {
    UsersPage getAllUsers(int page);
    UserDto addUser(SignUpDto signUpDto);

    UserDto getUser(Long userId);

    UserDto updateUser(Long userId, UpdateUserDto updateUser);

    void deleteUser(Long userId);

    UserDto publishUser(Long userId);
}
