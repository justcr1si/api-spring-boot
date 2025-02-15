package ru.project.project.service;

import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.project.project.repository.User;
import ru.project.project.repository.UserRepository;

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
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
        if (optionalUser.isPresent()) {
            throw new IllegalStateException("User already exists");
        }
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
    public void updatePassword(Long id, String password) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new IllegalStateException("User not found");
        }
        User user = optionalUser.get();
        if (password != null && !password.equals(user.getPassword())) {
            Optional<User> foundByUsername = userRepository.findByUsername(user.getUsername());
            if (foundByUsername.isPresent()) {
                throw new IllegalStateException("User already exists");
            }
            user.setPassword(password);
        }
    }
}
