package it.ca.telegrambotserver.telegramBot.bot.service;

import it.ca.telegrambotserver.telegramBot.bot.Register;
import it.ca.telegrambotserver.telegramBot.bot.entity.Start;
import it.ca.telegrambotserver.telegramBot.bot.entity.User;
import it.ca.telegrambotserver.telegramBot.bot.payload.ReqAdmin;
import it.ca.telegrambotserver.telegramBot.bot.payload.ResComment;
import it.ca.telegrambotserver.telegramBot.bot.payload.ResUsers;
import it.ca.telegrambotserver.telegramBot.bot.repository.StartRepository;
import it.ca.telegrambotserver.telegramBot.bot.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service

public class UserService {
    public boolean isAdd = false;

    private final UserRepository userRepository;
    private final StartRepository startRepository;

    public UserService(UserRepository userRepository, StartRepository startRepository) {
        this.userRepository = userRepository;
        this.startRepository = startRepository;
    }

    public void saveUser(User user) {
        User save = userRepository.save(user);
        checkStart(save.getChatId());
    }

    public void checkStart(Long chatId) {
        boolean existsUserByChatId = userRepository.existsUserByChatId(chatId);
        if (existsUserByChatId) {
            Start start = startRepository.findStartByChatId(chatId);
            startRepository.deleteById(start.getId());
        }
    }

    public List<ResUsers> getOdamlar() {
        List<User> all = userRepository.findAll();
        List<ResUsers> resUsersList = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            ResUsers resUsers = new ResUsers();
            resUsers.setTr(i + 1);
            resUsers.setId(all.get(i).getId());
            resUsers.setChatId(all.get(i).getChatId());
            resUsers.setFirstName(all.get(i).getFirstName());
            resUsers.setLastName(all.get(i).getLastName());
            resUsers.setPhoneNumber(all.get(i).getPhoneNumber());
            resUsers.setCourse(all.get(i).getCourse());
            resUsersList.add(resUsers);
        }
        return resUsersList;
    }

    public ResUsers getKorishOdam(Integer id) {
        Optional<User> byId = userRepository.findById(id);
        ResUsers resUsers = new ResUsers();
        if (byId.isPresent()) {
            User user = byId.get();
            resUsers.setTgFirstName(user.getTgFirstName());
            resUsers.setTgSurname(user.getTgSurname());
            resUsers.setTgUsername(user.getTgUsername());
        }
        return resUsers;

    }

    public void addComment(ResComment resComment) {
        Register.comments.add(resComment);
        isAdd = true;
    }

    public ReqAdmin getUser(Long chatId) {
        User user = userRepository.findByChatId(chatId);
        return new  ReqAdmin(user.getTgFirstName(), user.getTgUsername(), user.getChatId());
    }
}