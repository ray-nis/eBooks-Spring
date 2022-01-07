package com.eBooks.users;

import com.eBooks.exceptions.UserExistsException;
import com.eBooks.users.dto.UserSignupDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public User save(UserSignupDto userSignupDto) throws Exception {
        if (usernameExists(userSignupDto.getUsername())) {
            throw new UserExistsException();
        }

        Role userRole = roleRepository.findByRole("ROLE_USER");

        User user = User.builder()
                .username(userSignupDto.getUsername())
                .password(passwordEncoder.encode(userSignupDto.getPassword()))
                .roles(new HashSet<Role>(Arrays.asList(userRole)))
                .enabled(true)
                .nonLocked(true)
                .build();

        return userRepository.save(user);
    }

    public boolean usernameExists(String userName) {
        return userRepository.findByUsernameIgnoreCase(userName).isPresent();
    }
}
