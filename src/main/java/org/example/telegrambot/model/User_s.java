package org.example.telegrambot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User_s {
    private String chat_id;
    private String name;
    private String userName;
    private String phone;
    private UserTaype userTaype = UserTaype.START;
}
