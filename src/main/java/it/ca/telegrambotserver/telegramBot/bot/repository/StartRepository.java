package it.ca.telegrambotserver.telegramBot.bot.repository;

import it.ca.telegrambotserver.telegramBot.bot.entity.Start;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StartRepository extends JpaRepository<Start, Integer> {
    Start findStartByChatId(Long chatId);

    Start findByChatId(Long chatId);
}
