package ru.itis.javalab.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.javalab.dto.users.SignUpDto;
import ru.itis.javalab.dto.users.UpdateUserDto;
import ru.itis.javalab.dto.users.UserDto;
import ru.itis.javalab.dto.users.UsersPage;
import ru.itis.javalab.exceptions.NotFoundException;
import ru.itis.javalab.exceptions.SignUpException;
import ru.itis.javalab.models.User;
import ru.itis.javalab.repositories.UsersRepository;
import ru.itis.javalab.services.UserService;

import static ru.itis.javalab.dto.users.UserDto.from;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UsersRepository usersRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${default.page-size}")
    private int defaultPageSize;

    @Override
    public UsersPage getAllUsers(int page) {
        PageRequest pageRequest = PageRequest.of(page, defaultPageSize);

        Page<User> usersPage = usersRepository.findAllByStateOrderById(pageRequest, User.State.CONFIRMED);

        return UsersPage.builder()
                .users(from(usersPage.getContent()))
                .totalUsersCount(usersPage.getTotalPages())
                .build();
    }

    @Override
    public UserDto addUser(SignUpDto signUpDto) {
        if(usersRepository.findByEmail(signUpDto.getEmail()).isPresent()) {
            throw new SignUpException("Account with " + signUpDto.getEmail() + " email is present");
        }
        User user = User.builder()
                .firstName(signUpDto.getFirstName())
                .lastName(signUpDto.getLastName())
                .email(signUpDto.getEmail())
                .hashPassword(passwordEncoder.encode(signUpDto.getHashPassword()))
                .state(User.State.NOT_CONFIRMED)
                .build();
        usersRepository.save(user);

        return from(user);
    }

    @Override
    public UserDto getUser(Long userId) {
        User user = getOrElseThrow(userId);
        return from(user);
    }

    @Override
    public UserDto updateUser(Long userId, UpdateUserDto updateUser) {
        User userForUpdate = getOrElseThrow(userId);

        if (userForUpdate.getState().equals(User.State.DELETED)) {
            throw new NotFoundException("No user with id: <" + userId + "> found");
        }

        userForUpdate.setFirstName(updateUser.getFirstName());
        userForUpdate.setLastName(updateUser.getLastName());
        userForUpdate.setEmail(updateUser.getEmail());
        userForUpdate.setCardId(updateUser.getCardId());

        usersRepository.save(userForUpdate);
        return from(userForUpdate);
    }

    @Override
    public void deleteUser(Long userId) {
        User userToDelete = getOrElseThrow(userId);

        if (userToDelete.getState().equals(User.State.DELETED)) {
            throw new NotFoundException("No user with id: <" + userId + "> found");
        }

        userToDelete.setState(User.State.DELETED);
        usersRepository.save(userToDelete);
    }

    @Override
    public UserDto publishUser(Long userId) {
        User userToPublish = getOrElseThrow(userId);

        userToPublish.setState(User.State.CONFIRMED);
        userToPublish.setRole(User.Role.USER);
        usersRepository.save(userToPublish);

        return from(userToPublish);
    }

    private User getOrElseThrow(Long userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("No user with id: <" + userId + "> found"));
    }
}
