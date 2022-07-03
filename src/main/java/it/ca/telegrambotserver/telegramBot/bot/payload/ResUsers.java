package it.ca.telegrambotserver.telegramBot.bot.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResUsers {
    private Integer tr;
    private Integer id;
    private Long chatId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String course;

    private String tgFirstName;

    private String tgSurname;

    private String tgUsername;

    public ResUsers(Integer tr, Integer id, Long chatId, String firstName, String lastName, String phoneNumber, String course) {
        this.tr = tr;
        this.id = id;
        this.chatId = chatId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.course = course;
    }

    public ResUsers(String tgFirstName, String tgSurname, String tgUsername) {
        this.tgFirstName = tgFirstName;
        this.tgSurname = tgSurname;
        this.tgUsername = tgUsername;
    }
}
