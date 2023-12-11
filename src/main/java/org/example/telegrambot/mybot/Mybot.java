package org.example.telegrambot.mybot;

import lombok.SneakyThrows;
import org.example.telegrambot.button.ReplyMarkup1;
import org.example.telegrambot.button.Text;
import org.example.telegrambot.model.DbUrl;
import org.example.telegrambot.model.Todo;
import org.example.telegrambot.model.UserTaype;
import org.example.telegrambot.model.User_s;
import org.example.telegrambot.servis.Userservis;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Mybot extends TelegramLongPollingBot {
    public Mybot(String s){
        super(s);
    }
    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()){
            Message message = update.getMessage();
            String chatId = message.getChatId().toString();
            String text = message.getText();
            User_s users = Userservis.findChatId(chatId);
            if (users == null){
                if (message.hasContact()){
                    Contact contact = message.getContact();
                    User_s user_s = new User_s();
                    user_s.setChat_id(contact.getUserId().toString());
                    user_s.setName(contact.getFirstName()+ " " + contact.getLastName());

                    user_s.setUserName(message.getChat().getUserName());
                    user_s.setPhone(message.getContact().getPhoneNumber());
                    Userservis.dbQushish(user_s);
                    Connection connection = DbUrl.getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement("select * from user_update(?,?)");
                    preparedStatement.setString(1,chatId);
                    preparedStatement.setString(2,UserTaype.REGSTRED.name());
                    preparedStatement.execute();

                }else {
                   execute(ReplyMarkup1.contact(chatId));
                }
                return;
            }
            if(users.getUserTaype().equals(UserTaype.REGSTRED)){
                Connection connection = DbUrl.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("select * from user_update(?,?);");
                preparedStatement.setString(2, UserTaype.MAIN.toString());
                preparedStatement.setString(1,chatId);
                preparedStatement.execute();
                execute(ReplyMarkup1.MainMenyu(chatId));
            }
            else if (text.equals(Text.todo_ADD) && users.getUserTaype().equals(UserTaype.MAIN)){
                Connection connection = DbUrl.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("select * from user_update(?,?);");
                preparedStatement.setString(2,UserTaype.TODOQOSH.toString());
                preparedStatement.setString(1,chatId);
                preparedStatement.execute();
                execute(ReplyMarkup1.ADD_TODO(chatId));
            }
            else if (users.getUserTaype().equals(UserTaype.TODOQOSH)){

                execute(ReplyMarkup1.TODO_ADD(chatId,text));
                Connection connection = DbUrl.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("select * from user_update(?,?)");
                preparedStatement.setString(1,chatId);
                preparedStatement.setString(2,UserTaype.REGSTRED.name());
                preparedStatement.execute();


            }
            else if(text.equals(Text.todo_UCHIRISH)){
                Connection connection = DbUrl.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("select * from user_update(?,?)");
                preparedStatement.setString(1,chatId);
                preparedStatement.setString(2,UserTaype.REGSTRED.name());
                preparedStatement.execute();
                execute(ReplyMarkup1.Uchirish(chatId));
            }
        }else if (update.hasCallbackQuery()){
            String text = update.getCallbackQuery().getData();
            String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            User_s user_s = Userservis.findChatId(chatId);
            Connection connection = DbUrl.getConnection();
            Statement statement = connection.createStatement();
            String todo_find = "select * from todo s where s.user_id = '%s';";
            ResultSet resultSet = statement.executeQuery(String.format(todo_find,chatId));
            List<Todo> list1 = new ArrayList<>();
            if (resultSet != null) {
                while (resultSet.next()) {
                    Todo todo = new Todo();
                    todo.setUuid(resultSet.getObject("id", UUID.class));
                    todo.setName(resultSet.getString("name"));
                    todo.setUser_id(resultSet.getString("user_id"));
                    list1.add(todo);
                }
            }

            if(user_s.getUserTaype().equals(UserTaype.REGSTRED)){
                Todo todo1 = null;
                for (Todo todo : list1) {
                    if(todo.getName().equals(text)){
                        todo1 = todo;
                        break;
                    }
                }
                if (!(todo1==null)){
                    Connection connection1 = DbUrl.getConnection();
                    PreparedStatement preparedStatement = connection1.prepareStatement("select * from todo_delete(?)");
                    preparedStatement.setObject(1, todo1.getUuid());
                    preparedStatement.executeQuery();
                    preparedStatement.close();

                    SendMessage sendMessage = new SendMessage(chatId,"uchirildi");
                    execute(sendMessage);
                }else {
                    SendMessage sendMessage = new SendMessage(chatId,"oldin uchirilgan");
                    execute(sendMessage);
                }
            }
        }

    }

    @Override
    public String getBotUsername() {
        return "Chiroyli_kulguBot";
    }
}
