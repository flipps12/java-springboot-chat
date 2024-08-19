package com.example.demo.Users;

import com.example.demo.Repository.UsersRepository;
import com.example.demo.Users.Models.LoginUsers;
import com.example.demo.Users.Models.RegisterUsers;
import com.example.demo.Users.Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/auth")
@CrossOrigin(origins = {"https://react-chat-ws.vercel.app", "http://localhost:5173"})
@AllArgsConstructor
public class UsersController {

    private final UserService userService;
    private final UsersRepository usersRepository;

    @PostMapping(path = "register")
    public ResponseEntity<String> createAccount(@RequestBody RegisterUsers request) throws Exception {
        return ResponseEntity.ok(userService.registerService(request));
    }

    @PostMapping(path = "login")
    public ResponseEntity<String> verifyAccount(@RequestBody LoginUsers request) throws Exception {
        return ResponseEntity.ok(userService.loginService(request));
    }
}
