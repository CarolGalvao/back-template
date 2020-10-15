package com.template.back.controller;

import com.template.back.model.User;
import com.template.back.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping({"/user"})
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public ResponseEntity<List<User>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<User> findById(@PathVariable Long id){
        Optional<User> user = service.findById(id);
        if (!user.isPresent()) {
            log.error("Id " + id + " does not exist");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(user.get());
    }

    @GetMapping(path = {"/check-password/{email}/{password}"})
    public ResponseEntity<User> checkPassword(@PathVariable String email, @PathVariable String password){
        Optional<User> user = service.findByEmailAndPassword(email, password);
        if (!user.isPresent()) {
            log.error("Email or Password invalid");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(user.get());
    }

    @PostMapping
    public ResponseEntity create(@RequestBody User user){
        return ResponseEntity.ok(service.save(user));
    }

    @PutMapping(value="/{id}")
    public ResponseEntity update(@PathVariable("id") Long id,
                                 @RequestBody User user) {
        if (!service.findById(id).isPresent()) {
            log.error("Id " + id + " does not exist");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.save(user));
    }

    @DeleteMapping(path ={"/{id}"})
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!service.findById(id).isPresent()) {
            log.error("Id " + id + " does not exist");
            return ResponseEntity.badRequest().build();
        }
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
