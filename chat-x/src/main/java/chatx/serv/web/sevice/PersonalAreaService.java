package chatx.serv.web.sevice;

import chatx.serv.db.Db;
import chatx.serv.entities.ChatUser;
import chatx.serv.entities.ChatUserContainer;
import chatx.serv.utils.LOG;
import chatx.serv.utils.ResponseCode;
import chatx.serv.web.controller.AjaxMsg;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// *********************************************************************************************************************
public class PersonalAreaService {

    public static String 
            sqlSelectUserData = 
    "SELECT id, nick, email, first_name, last_name FROM users WHERE id=?";
    
    public static String 
            sqlInsertIntoUsers = 
    "INSERT INTO users(nick, first_name, last_name, email, pass, is_blocked) VALUES(?, ?, ?, ?, ?, 0) RETURNING id";

    // *****************************************************************************************************************
    
    public PersonalAreaService() {
    }
    // ***************************************************************************************************************** 
    
    public ChatUser  getUserRegistrationData (Long user_id, AjaxMsg amsg)
    {
        ChatUser newUser = new ChatUser ();
                                
        try (Connection        conn = Db._conn();
             PreparedStatement pstmt = conn.prepareStatement (sqlSelectUserData);)
        {
                    pstmt.setLong(1, user_id);
                    
                    ResultSet rs = pstmt.executeQuery();                                    
                    while (rs.next ())  { 
                        
                        Integer id    = rs.getInt("id");
                        String  nick  = rs.getString("nick");
                        String  email = rs.getString("email"); 
                        
                        String  firstname = rs.getString("first_name"); 
                        String  lastname =  rs.getString("last_name"); 

                        newUser.setId        (id);
                        newUser.setNick      (nick);
                        newUser.setEmail     (email);
                        newUser.setFirstName (firstname);
                        newUser.setLastName  (lastname);                        
                    }
        }
        catch (Exception e) {
            
            String err = "Пользователь с таким ID=" + user_id.longValue() + " не найден";
            System.out.println(err + ". Исключение при операции: " + e.getMessage());            
            amsg.setHeader("Регистрационные данные не найдены");
            amsg.setMessage("Сбой приложеня.<br>Попробуйте воспользоваться личным кабинетом позже. <br>" +
                            "Исключение при операции: " + e.getMessage());
            amsg.setResponseCode(ResponseCode.RESPONSE_CODE_ERROR);
            
            return newUser;            
        }
			
        amsg.setResponseCode(ResponseCode.RESPONSE_CODE_OK);
            
        return newUser;
    }
    // ***************************************************************************************************************** 

    private AjaxMsg createUser(ChatUser user, String pass, AjaxMsg amsg) throws SQLException {
        
        int id = -1;
        try (Connection conn = Db._conn();
             PreparedStatement pstmt = conn.prepareStatement (sqlInsertIntoUsers);)
        {
                    pstmt.setString (1, user.getNick());
                    pstmt.setString (2, user.getFirstName());
                    pstmt.setString (3, user.getLastName());
                    pstmt.setString (4, user.getEmail());
                    pstmt.setString (5, pass);

                    ResultSet rs = pstmt.executeQuery();                                    
                    while (rs.next ())  {                        
                        id = rs.getInt ("id");
                        
                        System.out.println(
                                "Создан новый пользователь Chat-X с ID=" + id + 
                                " с ником " + user.getNick() +
                                " и с емейлом " + user.getEmail()
                        );
                    }
        }
        catch (Exception e) {
            
            System.out.println("Создание пользователя невозможно. Исключение при операции: " + e.getMessage());            
            amsg.setHeader("Регистрация невозможна");
            amsg.setMessage("Сбой приложеня. Попробуйте зарегистрироваться позже");
            amsg.setResponseCode(ResponseCode.RESPONSE_CODE_ERROR);
            return amsg;            
        }
			
        fillSuccessMsg (user, amsg);        
        return amsg;
    }    
    // ***************************************************************************************************************** 
    
    private void fillSuccessMsg (ChatUser user, AjaxMsg amsg) {
        
        amsg.setHeader("Добро пожаловать Chat-X!");
        amsg.setMessage(
                
                "Пользователь с ником '" + user.getNick() + "' и емейлом '" + user.getEmail()+ "'успешно создан!<br>" +
                "Перейдите по ссылке в  личный кабинет <a href=''>personal-area</a> или <br>" +
                "зайдите через страницу аутентификации <a href=''>login</a> ");
        
        amsg.setResponseCode(ResponseCode.RESPONSE_CODE_OK);
    }    
    // ***************************************************************************************************************** 
    
    private AjaxMsg isNickOccupied (List<ChatUser> userList, String nick, AjaxMsg amsg) {
    
        // сначала в мап, используя Java 8 streams and collectors, а потом - по ключу проверим
        Map<String, ChatUser> map;
        
        map = userList .stream ()
                       .collect (Collectors.toMap (ChatUser::getNick, user -> user));

        if (!map.containsKey(nick.toLowerCase())) 
            return amsg;
        
        amsg.setHeader("Регистрация невозможна");
        amsg.setMessage("Причина: Пользователь с ником '" + nick + "' уже существет");
        amsg.setResponseCode(ResponseCode.RESPONSE_CODE_ERROR);
        
        return amsg;
    }    
    // ***************************************************************************************************************** 

    private AjaxMsg isEmailOccupied (List<ChatUser> userList, String email, AjaxMsg amsg) {
    
        // сначала в мап, используя Java 8 streams and collectors, а потом - по ключу проверим
        Map<String, ChatUser> map;
        
        map = userList .stream ()
                       .collect (Collectors.toMap (ChatUser::getEmail, user -> user));

        if (!map.containsKey(email.toLowerCase())) 
            return amsg;
        
        amsg.setHeader("Регистрация невозможна");
        amsg.setMessage("Причина: Пользователь с емейлом '" + email + "' уже существет");
        amsg.setResponseCode(ResponseCode.RESPONSE_CODE_ERROR);
        
        return amsg;
    }
    // *****************************************************************************************************************     
}

