package com.example;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Base64;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {


    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public boolean login(@RequestBody User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        return existingUser.isPresent() &&
                user.getPassword().equals(existingUser.get().getPassword());
    }
    @PostMapping("/users")
    public User register(@RequestBody User user) {
        return userRepository.save(user);
    }


    @GetMapping("/login")
    public User login(HttpServletRequest request) throws NotFoundException {
        String authToken = request.getHeader("Authorization")
                .substring("Basic".length()).trim();
        String email = authToken.split(":")[0];

        String password = authToken.split(":")[1];

        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent() &&
            existingUser.get().getPassword().equals(password)) {
            return  existingUser.get();
        }
         else {
             throw new NotFoundException("Invalid email or password ");
        }
    }
}
