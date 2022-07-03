package it.ca.telegrambotserver.telegramBot.bot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Long chatId;

    //foydalanuvchidan malumot olish
    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String course;


    //telegram malumotlar
    private String tgFirstName;

    private String tgSurname;

    private String tgUsername;

    public User(Long chatId, String firstName, String lastName, String phoneNumber, String tgFirstName, String tgSurname, String tgUsername, String course) {
        this.chatId = chatId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.tgFirstName = tgFirstName;
        this.tgSurname = tgSurname;
        this.tgUsername = tgUsername;
        this.course = course;
    }
}
