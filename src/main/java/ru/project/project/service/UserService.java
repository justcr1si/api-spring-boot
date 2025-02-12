package ru.project.project.service;

import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.project.project.repository.User;
import ru.project.project.repository.UserRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Cacheable(value = "users")
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User create(User user) {
        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
        if (optionalUser.isPresent()) {
            throw new IllegalStateException("User already exists");
        }
        user.setAge(Period.between(user.getDob(), LocalDate.now()).getYears());
        return userRepository.save(user);
    }

    public String delete(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new IllegalStateException("User not found");
        }
        userRepository.deleteById(id);
        return "User has been successfully deleted";
    }

    @Transactional
    public void update(Long id, String email, String name) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new IllegalStateException("User not found");
        }
        User user = optionalUser.get();
        if (email != null && !email.equals(user.getEmail())) {
            Optional<User> foundByEmail = userRepository.findByEmail(email);
            if (foundByEmail.isPresent()) {
                throw new IllegalStateException("User already exists");
            }
            user.setEmail(email);
        }

        if (name != null && !name.equals(user.getName())) {
            user.setName(name);
        }
    }
}
