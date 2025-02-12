package ru.project.project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.project.project.repository.User;
import ru.project.project.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/users")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.create(user);
    }

    @DeleteMapping(path = "{id}")
    public String delete(@PathVariable Long id) {
        return userService.delete(id);
    }

    @PutMapping(path = "{id}")
    public void update(@PathVariable Long id,
                       @RequestParam(required = false) String email,
                       @RequestParam(required = false) String name) {
         userService.update(id, email, name);
    }
}
