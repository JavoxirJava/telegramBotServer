package it.ca.telegrambotserver.telegramBot.bot.model;

import java.util.*;

public interface ButtonRegister {
    List<String> REGISTER = Arrays.asList("register", "back");
    List<String> ADMIN_BUTTON = Arrays.asList("sendAdvertising", "addAdmin", "adminList", "deleteAdmin", "addCourse", "deleteCourse");
    List<String> MIN_ADMIN = Arrays.asList("sendAdvertising", "adminList", "addCourse", "deleteCourse");
    List<String> ADMIN_TAN = Arrays.asList("start", "user", "all", "back");
    String ADMIN = "1085241246";
    List<String> BACK = Collections.singletonList("back");
    String BOT_USER_NAME = "@IT_City_Academy_Register_bot";
    String BOT_TOKEN = "5483301574:AAEHea_I606cc_6fMUYGbSDho5OZIPhF2DI";
    String PHONE_NUMBER = "+998993393300";
    String TELEGRAM_LINK = "@ncjkdcns";
}