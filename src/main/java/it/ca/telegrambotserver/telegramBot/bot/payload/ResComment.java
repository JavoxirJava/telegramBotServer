package it.ca.telegrambotserver.telegramBot.bot.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResComment {
    private String text;
    private String rasm;
}
