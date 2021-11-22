package com.eBooks.shared.dev;

import com.eBooks.users.RoleService;
import com.eBooks.users.UserService;
import com.eBooks.users.dto.UserSignupDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final UserService userService;
    private final RoleService roleService;

    @Override
    public void run(String... args) throws Exception {
        roleService.create("ROLE_USER");

        userService.save(UserSignupDto.builder()
                .username("FirstUser")
                .password("password")
                .matchingPassword("password")
                .build());
    }
}
