package it.ca.telegrambotserver;

import it.ca.telegrambotserver.telegramBot.bot.Register;
import it.ca.telegrambotserver.telegramBot.bot.repository.AdminRepository;
import it.ca.telegrambotserver.telegramBot.bot.repository.CourseRepository;
import it.ca.telegrambotserver.telegramBot.bot.repository.StartRepository;
import it.ca.telegrambotserver.telegramBot.bot.repository.UserRepository;
import it.ca.telegrambotserver.telegramBot.bot.service.AdminService;
import it.ca.telegrambotserver.telegramBot.bot.service.CourseService;
import it.ca.telegrambotserver.telegramBot.bot.service.StartService;
import it.ca.telegrambotserver.telegramBot.bot.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class TelegramBotServerApplication {

    public static void main(String[] args) throws TelegramApiException {
        ConfigurableApplicationContext run = SpringApplication.run(TelegramBotServerApplication.class, args);
        UserRepository userRepository = run.getBean(UserRepository.class);
        UserService userService = run.getBean(UserService.class);
        StartService startService = run.getBean(StartService.class);
        StartRepository startRepository = run.getBean(StartRepository.class);
        CourseRepository courseRepository = run.getBean(CourseRepository.class);
        CourseService courseService = run.getBean(CourseService.class);
        AdminRepository adminService = run.getBean(AdminRepository.class);
        AdminService adminRepository = run.getBean(AdminService.class);
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new Register(startRepository, startService, userRepository, userService, courseService, courseRepository, adminService, adminRepository));
    }
}