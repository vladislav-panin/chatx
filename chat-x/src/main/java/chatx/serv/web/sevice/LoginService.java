package chatx.serv.web.sevice;

import chatx.serv.db.Db;
import chatx.serv.entities.ChatUserContainer;
import chatx.serv.entities.ChatUser;
import chatx.serv.utils.LOG;
import chatx.serv.utils.ResponseCode;
import chatx.serv.web.controller.AjaxMsg;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class LoginService {
    
    public static String 
            sqlSelectUser = 
    "SELECT " +            
        "id, " +
        "nick," +
        "first_name," +
        "last_name, " +
        "email, " +
        "is_blocked " +
    "FROM " +
        "users " +
    "WHERE " +
        "nick='%s' AND pass='%s'";
    
    //******************************************************************************************************************    
    public AjaxMsg login (ChatUserContainer uCont, String pass) 
    {        
        AjaxMsg ret = new AjaxMsg ();
        
        ChatUser user = uCont.getUser();
        ChatUser restoredUser = getUser (user, pass);
        
        if (null == restoredUser) {
            
            ret.setResponseCode(ResponseCode.RESPONSE_CODE_ERROR);
            ret.setHeader("Ошибка аутентификации");
            ret.setMessage("Такое сочетание пользователь/пароль не найдено!");
            
            return ret;
        }
        
        ret.setResponseCode(ResponseCode.RESPONSE_CODE_OK);
        ret.setHeader("Аутентификация прошла успешно");
        ret.setMessage("Добро пожалjвать в Chat-X!");
        
        uCont.setUser(restoredUser);      
        
        return ret;    
    }
    //****************************************************************************************************************** 
    public ChatUser getUser(ChatUser user, String pass)
    {
        String sql = String.format (sqlSelectUser, user.getNick(), pass);
        
        List<ChatUser> userList = new ArrayList<> ();
        try (Connection conn = Db._conn();)
        {
            try (Statement st = conn.createStatement();)
            {  
                ResultSet rs = st.executeQuery(sql); 
                
                while (rs.next()) 
                {
                    ChatUser newUser = new ChatUser ();
                    
                    Integer id = rs.getInt("id");
                    Integer blocked = rs.getInt("is_blocked");
                    
                    String  nick       = rs.getString("nick");
                    String  first_name = rs.getString("first_name");
                    String  last_name  = rs.getString("last_name");
                    String  email      = rs.getString("email");                    
                    
                    newUser.setId        (id);
                    newUser.setNick      (nick);
                    newUser.setFirstName (first_name);
                    newUser.setLastName  (last_name);
                    newUser.setEmail     (email);
                    newUser.setBlocked   (blocked == 0 ? false : true);
                    
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
        
        int sz = userList.size();
        
        switch (sz)
        {
            case 0: 
                return null;
                
            case 1: 
                return userList.get(0);
        }
            
        return null;
    }
    //****************************************************************************************************************** 
}
