package com.example.controllers;

import com.example.entities.User;
import com.example.errors.APIMessage;
import com.example.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class UserController {
    @Autowired
    private UserRepo userRepo;
//Проверка на авторизацию
    @GetMapping("/api/loginstatus")
    public Object status(HttpSession session) {
        if (session.getAttribute("UserLogin") == null) {
            return APIMessage.Unauthorized;
        } else {
            return APIMessage.OK(session.getAttribute("UserLogin").toString());
        }
    }
//Регистрация
    @GetMapping("/api/register")
    public Object register(@RequestParam String login,
                           @RequestParam String password,
                           HttpSession session) {
        if (session.getAttribute("UserLogin") != null) {
            return APIMessage.LoggedInAlready;  // Уже авторизован
        }
        if (userRepo.findByLogin(login) != null) {
            return APIMessage.LoginIsBusy;  //Логин уже существует
        }

        User user = new User(login, password);
        userRepo.save(user);    // Если все ок, добавляем пользователя в БД
        session.setAttribute("UserLogin", login);

        return APIMessage.OK;   //Возвращаем ОК
    }

    @GetMapping("/api/login")
    public Object login(@RequestParam String login,
                        @RequestParam String password,
                        HttpSession session) {
        if (session.getAttribute("UserLogin") != null) {
            return APIMessage.LoggedInAlready;
        }
        if (userRepo.findByLoginAndPassword(login, password) == null) {
            return APIMessage.IncorrectCredentials;
        }
        session.setAttribute("UserLogin", login);
        return APIMessage.OK;
    }

    @GetMapping("/api/logout")
    public Object logout(HttpSession session) {
        session.removeAttribute("UserLogin");
        return APIMessage.OK;
    }
}
