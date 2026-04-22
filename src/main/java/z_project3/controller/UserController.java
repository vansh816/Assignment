package z_project3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import z_project3.model.User;
import z_project3.repository.UserRepository;
import z_project3.service.UserService;

@RestController
@RequestMapping
public class UserController {

 @Autowired
    private UserService userService;

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        User saved = userService.create(user);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return new ResponseEntity<>(userService.findall(), HttpStatus.OK);
    }


}
