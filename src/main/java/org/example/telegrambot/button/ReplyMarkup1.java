package org.example.telegrambot.button;


import lombok.SneakyThrows;
import org.example.telegrambot.model.DbUrl;
import org.example.telegrambot.model.Todo;
import org.example.telegrambot.model.UserTaype;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;


import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReplyMarkup1 {

    @SneakyThrows
    public static SendMessage contact(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "Telefon raqam kirting");
        KeyboardRow keyboardRow = new KeyboardRow();
        List<KeyboardRow> list = new ArrayList<>();
        KeyboardButton keyboardButton = new KeyboardButton();
        keyboardButton.setText("\uD83D\uDCF1 Telfon nomer");
        keyboardButton.setRequestContact(true);
        keyboardRow.add(keyboardButton);
        list.add(keyboardRow);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setKeyboard(list);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        return sendMessage;


    }

    @SneakyThrows
    public static SendMessage MainMenyu(String chatId) {

        SendMessage sendMessage = new SendMessage(chatId, "MainMenyu");
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> list = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(Text.todo_ADD);
        list.add(keyboardRow);
        keyboardRow = new KeyboardRow();
        keyboardRow.add(Text.todo_UCHIRISH);
        list.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(list);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return sendMessage;

    }

    @SneakyThrows
    public static SendMessage ADD_TODO(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "Vazifa nomini kirting");
        return sendMessage;
    }

    @SneakyThrows
    public static SendMessage TODO_ADD(String chatId, String text) {
        SendMessage sendMessage = new SendMessage(chatId, "todo");
        Connection connection = DbUrl.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("insert into todo(name,user_id) values (?,?)");
        preparedStatement.setString(1, text);
        preparedStatement.setString(2, chatId);
        preparedStatement.execute();
        sendMessage.setText("muvaqiyatli qushildi");
        return sendMessage;

    }

    @SneakyThrows
    public static SendMessage Uchirish(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "Uchirmoqchi bulgan todo ustiga bosing");
        Connection connection = DbUrl.getConnection();
        Statement statement = connection.createStatement();
        String todo_find = "select * from todo s where s.user_id = '%s';";
        ResultSet resultSet = statement.executeQuery(String.format(todo_find, chatId));
        List<Todo> list1 = new ArrayList<>();
        if (resultSet.next()) {
            while (resultSet.next()) {
                Todo todo = new Todo();
                todo.setUuid(resultSet.getObject("id", UUID.class));
                todo.setName(resultSet.getString("name"));
                todo.setUser_id(resultSet.getString("user_id"));
                list1.add(todo);
            }


            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> inlineKeyboardButtons = new ArrayList<>();
            List<InlineKeyboardButton> list = new ArrayList<>();

            for (Todo todo : list1) {
                InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();

                inlineKeyboardButton.setText(todo.getName());
                inlineKeyboardButton.setCallbackData(todo.getName());
                list.add(inlineKeyboardButton);
                inlineKeyboardButtons.add(list);
                list = new ArrayList<>();
            }
            inlineKeyboardMarkup.setKeyboard(inlineKeyboardButtons);
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);

            return sendMessage;
        } else {
            sendMessage.setText("Todo ruhhat bush ");
            Connection connection1 = DbUrl.getConnection();
            PreparedStatement preparedStatement = connection1.prepareStatement("select * from user_update(?,?)");
            preparedStatement.setString(1, chatId);
            preparedStatement.setString(2, UserTaype.REGSTRED.name());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return sendMessage;
        }


    }

}
