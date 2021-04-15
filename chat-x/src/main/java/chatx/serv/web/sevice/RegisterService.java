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
public class RegisterService {

    public static String 
            sqlSelectUser = 
    "SELECT id, nick, email FROM users WHERE nick='%s' OR email='%s'";
    
    public static String 
            sqlInsertIntoUsers = 
    "INSERT INTO users(nick, first_name, last_name, email, pass, is_blocked) VALUES(?, ?, ?, ?, ?, 0) RETURNING id";

    // *****************************************************************************************************************
    
    public RegisterService() {
    }

    // *****************************************************************************************************************
    public AjaxMsg register (ChatUserContainer uCont, String pass) throws SQLException {
         
        AjaxMsg ret = new AjaxMsg ();
        
        ChatUser user = uCont.getUser();
        List <ChatUser> userList = getUserList (user);
        
        if (userList.isEmpty()) {            
            ret = createUser (user, pass, ret);            
            return ret;
        }
        
        ret = isNickOccupied (userList, user.getNick(), ret);
        if (ret.getResponseCode() == ResponseCode.RESPONSE_CODE_ERROR)
            return ret;
        
        ret = isEmailOccupied (userList, user.getEmail(), ret);
        if (ret.getResponseCode() == ResponseCode.RESPONSE_CODE_ERROR)
            return ret;
            
        return ret;    
    }
    // ***************************************************************************************************************** 
    
    public List <ChatUser>  getUserList(ChatUser user)
    {
        String sql = String.format (sqlSelectUser, user.getNick(), user.getEmail());
        
        List<ChatUser> userList = new ArrayList<> ();
        try (Connection conn = Db._conn();)
        {
            try (Statement st = conn.createStatement();)
            {  
                ResultSet rs = st.executeQuery(sql); 
                
                while (rs.next()) 
                {
                    ChatUser newUser = new ChatUser ();
                    
                    Integer id    = rs.getInt("id");
                    String  nick  = rs.getString("nick");
                    String  email = rs.getString("email");                    
                    
                    newUser.setId        (id);
                    newUser.setNick      (nick.toLowerCase());
                    newUser.setEmail     (email.toLowerCase());
                    
                    userList.add(newUser);
                }
            }            
            catch (SQLException ex) {                       
                LOG._err(ex.getMessage());
            }                
        }
        catch (Exception ex) {   
            LOG._err(ex.getMessage());
        } 
            
        return userList;
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

