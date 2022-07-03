package it.ca.telegrambotserver.telegramBot.bot.repository;

import it.ca.telegrambotserver.telegramBot.bot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByChatIdAndCourseIgnoreCase(Long chatId, String course);

    boolean existsUserByChatId(Long chatId);

    User findByChatId(Long chatId);
}
