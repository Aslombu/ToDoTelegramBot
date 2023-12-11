package org.example.telegrambot.servis;

import lombok.SneakyThrows;
import org.example.telegrambot.model.DbUrl;
import org.example.telegrambot.model.UserTaype;
import org.example.telegrambot.model.User_s;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class Userservis {

    public static User_s findChatId(String b_chatId) throws SQLException {
        Connection connection = DbUrl.getConnection();
        Statement statement = connection.createStatement();

        String users_find = "select * from users s where s.chatid = '%s';";
        ResultSet resultSet = statement.executeQuery(String.format(users_find,b_chatId));
        if (resultSet.next()) {
            User_s user_s = new User_s();
            user_s.setChat_id(resultSet.getString("chatid"));
            user_s.setName(resultSet.getString("name"));
            user_s.setUserName(resultSet.getString("username"));
            user_s.setUserTaype(UserTaype.valueOf(resultSet.getString("userstat")));
            user_s.setPhone(resultSet.getString("phone"));

            return user_s;
        }
        return null;
    }

    @SneakyThrows
    public static void dbQushish(User_s userS) {
        Connection connection = DbUrl.getConnection();
        Statement statement = connection.createStatement();
        String str = " select * from add_users('%s','%s','%s','%s','%s');";
        statement.execute(String.format(str,userS.getChat_id(),userS.getName(),userS.getUserName(),userS.getPhone(),userS.getUserTaype()));
    }
}
