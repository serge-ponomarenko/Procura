package ua.ltd.procura.procuraapp.service;

import ua.ltd.procura.procuraapp.dto.UserDto;
import ua.ltd.procura.procuraapp.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void saveUser(UserDto userDto);

    Optional<User> findByEmail(String email);

    List<UserDto> findAllUsers();

    void processOAuthPostLogin(String name, String email);
}
