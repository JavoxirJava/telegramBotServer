package it.ca.telegrambotserver.telegramBot.bot.service;

import it.ca.telegrambotserver.telegramBot.bot.Register;
import it.ca.telegrambotserver.telegramBot.bot.entity.Start;
import it.ca.telegrambotserver.telegramBot.bot.entity.User;
import it.ca.telegrambotserver.telegramBot.bot.payload.ReqAdmin;
import it.ca.telegrambotserver.telegramBot.bot.payload.ResComment;
import it.ca.telegrambotserver.telegramBot.bot.payload.ResStart;
import it.ca.telegrambotserver.telegramBot.bot.repository.StartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service

public class StartService {
    public boolean isAdd1 = false;


    private final StartRepository startRepository;

    public StartService(StartRepository startRepository) {
        this.startRepository = startRepository;
    }

    public void saveStart(Start start) {
            startRepository.save(start);
    }

    public List<ResStart> getStart(){
        List<Start> all = startRepository.findAll();
        List<ResStart> resStartsList = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            ResStart resStarts = new ResStart();
            resStarts.setTr(i+1);
            resStarts.setChatId(all.get(i).getChatId());
            resStarts.setFirstName(all.get(i).getFirstName());
            resStarts.setUsername(all.get(i).getUsername());
            resStartsList.add(resStarts);
        }
        return resStartsList;
    }

    public ReqAdmin getStart(Long chatId) {
        Start start = startRepository.findByChatId(chatId);
        return new ReqAdmin(start.getFirstName(), start.getUsername(), start.getChatId());
    }

    public void addComment(ResComment resComment) {
        Register.comments.add(resComment);
        isAdd1 = true;
    }
}