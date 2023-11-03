package com.example.shopapi.runners;

import com.example.shopapi.enums.Role;
import com.example.shopapi.models.User;
import com.example.shopapi.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminUserSeedRunner implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Environment env;

    public AdminUserSeedRunner(UserRepository userRepository, PasswordEncoder passwordEncoder, Environment env) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.env = env;
    }

    @Override
    public void run(String... args) {
        User adminUser = new User();
        adminUser.setEmail(env.getProperty("admin.email"));
        adminUser.setPassword(passwordEncoder.encode(env.getProperty("admin.password")));
        adminUser.setRole(Role.ADMIN);
        userRepository.save(adminUser);
    }
}
