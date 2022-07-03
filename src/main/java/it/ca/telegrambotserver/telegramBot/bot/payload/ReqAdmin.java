package it.ca.telegrambotserver.telegramBot.bot.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqAdmin {
    private String firstName;
    private String username;
    private Long chatId;
}
