package ua.ltd.procura.procuraapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.ltd.procura.procuraapp.dto.UserDto;
import ua.ltd.procura.procuraapp.entity.Role;
import ua.ltd.procura.procuraapp.entity.User;
import ua.ltd.procura.procuraapp.repository.RoleRepository;
import ua.ltd.procura.procuraapp.repository.UserRepository;
import ua.ltd.procura.procuraapp.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void saveUser(UserDto userDto) {
        Role role = checkAdminRoleExist();

        User user = User.builder()
                .name(userDto.getFirstName() + " " + userDto.getLastName())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .roles(List.of(role))
                .provider(User.Provider.LOCAL)
                .build();
        userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertEntityToDto)
                .toList();
    }

    private UserDto convertEntityToDto(User user) {
        String[] name = user.getName().split(" ");
        return UserDto.builder()
                .firstName(name[0])
                .lastName(name[1])
                .email(user.getEmail())
                .build();
    }

    private Role checkAdminRoleExist() {
        return roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> {
            Role result = new Role();
            result.setName("ROLE_ADMIN");
            return roleRepository.save(result);
        });

    }

    @Override
    public void processOAuthPostLogin(String name, String email) {
        userRepository.findByEmail(email).ifPresentOrElse(user -> { },
                () -> {
                    Role role = checkAdminRoleExist();

                    User user = User.builder()
                            .name(name)
                            .email(email)
                            .password("TEMP")  //todo: need to handle this
                            .provider(User.Provider.GOOGLE)
                            .roles(List.of(role))
                            .build();

                    userRepository.save(user);

                }
        );
    }
}
