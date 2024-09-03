package com.example.demo.Users.Services;

import com.example.demo.Repository.UsersRepository;
import com.example.demo.Users.Component.AESUtil;
import com.example.demo.Users.Component.JwtUtil;
import com.example.demo.Users.Models.LoginUsers;
import com.example.demo.Users.Models.RegisterUsers;
import com.example.demo.Users.Users;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final JwtUtil jwtUtil;
    private final AESUtil aesUtil;
    private final UsersRepository usersRepository;

    private String validateLength(String value, int min, int max) { // validado de numero de caracteres
        if (value.length() >= max || value.length() <= min) {
            return "numOfCharacters";
        }
        return null;
    }

    // registro de usuario
    public String registerService (RegisterUsers user) throws Exception {
        int topChar = 20;
        int minChat = 3;
        String result;
        if ((result = validateLength(user.getUsername(), minChat, topChar)) != null) return result;
        if ((result = validateLength(user.getName(), minChat, topChar)) != null) return result;
        if ((result = validateLength(user.getPassword(), minChat, topChar)) != null) return result;
        String passwordEncoded = aesUtil.encrypt(user.getPassword().toString());
        if (usersRepository.findByUsername(user.getUsername()).isPresent()) return "user";
        Users userSave = Users.builder()
                .name(user.getName())
                .username(user.getUsername())
                .password(passwordEncoded)
                .build();
        usersRepository.save(userSave);

        return jwtUtil.generateToken(user.getUsername()); // cambiar secret key
    }

    // inicio de sesion
    public String loginService (LoginUsers user) throws Exception {
        if (usersRepository.findByUsername(user.getUsername()).isEmpty()) return "user";
        if (validatePassword(user.getUsername(), user.getPassword())) return "password";

        return jwtUtil.generateToken(user.getUsername());
    }

    // validar constraseña -- true (no coincide) false (coincide)
    public boolean validatePassword(String user, String password) throws Exception {
        String passwordEncode = usersRepository.findByUsername(user).get().getPassword();
        if (password.equals(aesUtil.decrypt(passwordEncode))) return false;
        return true;
    }
}
