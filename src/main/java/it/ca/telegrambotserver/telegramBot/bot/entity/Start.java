package it.ca.telegrambotserver.telegramBot.bot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Start {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Long chatId;

    @Column(nullable = false)
    private String firstName;

    @Column(unique = true)
    private String username;

    public Start(Long chatId, String firstName, String username) {
        this.chatId = chatId;
        this.firstName = firstName;
        this.username = username;
    }
}
