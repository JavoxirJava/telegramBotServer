package it.ca.telegrambotserver.telegramBot.bot.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResStart {
    private Integer tr;
    private Integer id;
    private Long chatId;
    private String firstName;
    private String username;
}
