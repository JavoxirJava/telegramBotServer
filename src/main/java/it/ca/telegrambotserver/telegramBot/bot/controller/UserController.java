package it.ca.telegrambotserver.telegramBot.bot.controller;

import it.ca.telegrambotserver.telegramBot.bot.Register;
import it.ca.telegrambotserver.telegramBot.bot.entity.User;
import it.ca.telegrambotserver.telegramBot.bot.payload.ResComment;
import it.ca.telegrambotserver.telegramBot.bot.payload.ResUsers;
import it.ca.telegrambotserver.telegramBot.bot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping
    public String getCourse() {
        return "user";
    }

    @PostMapping()
    @ResponseBody
    public void addUsers(@RequestBody User user){
        userService.saveUser(user);
    }

    @GetMapping("/list")
    @ResponseBody
    public List<ResUsers> getOdamlar(){
        return userService.getOdamlar();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResUsers getTgOdam(@PathVariable Integer id){
        return userService.getKorishOdam(id);
    }

    @PostMapping("/comment")
    @ResponseBody
    public void addComment(@RequestBody ResComment resComment){
        userService.addComment(resComment);
    }
}