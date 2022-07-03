package it.ca.telegrambotserver.telegramBot.bot.controller;

import it.ca.telegrambotserver.telegramBot.bot.entity.Start;
import it.ca.telegrambotserver.telegramBot.bot.payload.ResComment;
import it.ca.telegrambotserver.telegramBot.bot.payload.ResStart;
import it.ca.telegrambotserver.telegramBot.bot.service.StartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/start")
public class StartController {

    @Autowired
    StartService startService;

    @GetMapping
    public String getCourse() {
        return "start";
    }

    @PostMapping()
    @ResponseBody
    public void addUsers(@RequestBody Start start){
        startService.saveStart(start);
    }

    @GetMapping("/list")
    @ResponseBody
    public List<ResStart> getStart(){
        return startService.getStart();
    }

    @PostMapping("/comment")
    @ResponseBody
    public void addComment(@RequestBody ResComment resComment){
        startService.addComment(resComment);
    }
}