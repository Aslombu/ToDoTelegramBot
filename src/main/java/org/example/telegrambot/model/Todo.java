package org.example.telegrambot.model;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Todo {
    private UUID uuid;
    private String name;
    private String user_id;

}
