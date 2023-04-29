package com.driver.controllers;
import com.driver.model.User;
import com.driver.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserServiceImpl userService;
    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestParam String name, @RequestParam String phoneNumber, @RequestParam String password){
        //add a user with the given name, phone number, and password
        userService.register(name, phoneNumber, password);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<User> updatePassword(@RequestParam Integer userId, @RequestParam String password){
        //Update the password of the user with the given userId
        User updatedUser = userService.updatePassword(userId, password);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public void deleteUser(@RequestParam Integer userId){
        //Delete the user with the given userId
        userService.deleteUser(userId);
    }
}
