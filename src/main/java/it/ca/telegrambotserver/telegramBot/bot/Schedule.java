package it.ca.telegrambotserver.telegramBot.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
public class Schedule {
    @Autowired
    Register register;

    @Scheduled(cron = "0 1 0 * * *")
    public void isComments() {
        register.isComment();
    }

}
