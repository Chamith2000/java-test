package com.newsportal.config;

import com.newsportal.entity.*;
import com.newsportal.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final NewsRepository newsRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        Role adminRole = createRoleIfNotExists(Role.ERole.ROLE_ADMIN);
        Role editorRole = createRoleIfNotExists(Role.ERole.ROLE_EDITOR);
        Role readerRole = createRoleIfNotExists(Role.ERole.ROLE_READER);

        createUserIfNotExists("admin", "admin@newsportal.com", "Admin@123", Set.of(adminRole));
        createUserIfNotExists("editor", "editor@newsportal.com", "Editor@123", Set.of(editorRole));
        createUserIfNotExists("reader", "reader@newsportal.com", "Reader@123", Set.of(readerRole));

        Category sports = createCategoryIfNotExists("Sports");
        Category entertainment = createCategoryIfNotExists("Entertainment");
        Category politics = createCategoryIfNotExists("Politics");
        Category technology = createCategoryIfNotExists("Technology");

        if (newsRepository.count() == 0) {
            User editor = userRepository.findByUsername("editor").orElseThrow();

            News news1 = News.builder()
                    .title("Sri Lanka lost the cricket world cup")
                    .content("In a dramatic final, Sri Lanka narrowly lost the Cricket World Cup final. " +
                            "Fans across the country expressed a mix of pride and disappointment as the team " +
                            "fought hard until the very last over.")
                    .author(editor)
                    .categories(new HashSet<>(Set.of(sports, entertainment)))
                    .build();

            News news2 = News.builder()
                    .title("New flagship smartphone launched with on-device AI")
                    .content("A leading manufacturer unveiled its newest flagship smartphone today, headlined by " +
                            "an on-device AI assistant, a redesigned camera system, and a significantly larger battery.")
                    .author(editor)
                    .categories(new HashSet<>(Set.of(technology)))
                    .build();

            News news3 = News.builder()
                    .title("Parliament passes new digital economy bill")
                    .content("Lawmakers passed a new bill aimed at boosting the digital economy, including tax " +
                            "incentives for tech startups and expanded broadband access in rural areas.")
                    .author(editor)
                    .categories(new HashSet<>(Set.of(politics, technology)))
                    .build();

            newsRepository.save(news1);
            newsRepository.save(news2);
            newsRepository.save(news3);
            log.info("Seeded {} sample news items", 3);
        }

        log.info("Data initialization complete.");
    }

    private Role createRoleIfNotExists(Role.ERole roleName) {
        return roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.save(Role.builder().name(roleName).build()));
    }

    private void createUserIfNotExists(String username, String email, String rawPassword, Set<Role> roles) {
        if (userRepository.existsByUsername(username)) {
            return;
        }
        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .enabled(true)
                .roles(new HashSet<>(roles))
                .build();
        userRepository.save(user);
        log.info("Created default user '{}' with role(s) {}", username, roles);
    }

    private Category createCategoryIfNotExists(String name) {
        return categoryRepository.findByName(name)
                .orElseGet(() -> categoryRepository.save(Category.builder().name(name).build()));
    }
}
