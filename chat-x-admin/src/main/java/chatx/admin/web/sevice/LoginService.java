package chatx.admin.web.sevice;

import chatx.admin.db.Db;
import chatx.admin.entities.AdminContainer;
import chatx.admin.entities.Administrator;
import chatx.admin.utils.LOG;
import chatx.admin.utils.ResponseCode;
import chatx.admin.web.controller.AjaxMsg;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class LoginService {
    
    public static String 
            sqlSelectAdmin = 
    "SELECT " +            
        "id, " +
        "nick," +
        "first_name," +
        "last_name, " +
        "email, " +
        "is_blocked " +
    "FROM " +
        "admins " +
    "WHERE " +
        "nick='%s' AND pass='%s'";
    
    //******************************************************************************************************************    
    public AjaxMsg login (AdminContainer aCont, String pass) 
    {        
        AjaxMsg ret = new AjaxMsg ();
        
        Administrator admin = aCont.getAdmin();
        Administrator restoredAdmin = getAdmin (admin, pass);
        
        if (null == restoredAdmin) {
            
            ret.setResponseCode(ResponseCode.RESPONSE_CODE_ERROR);
            ret.setHeader("Ошибка аутентификации");
            ret.setMessage("Такое сочетание администратор/пароль не найдено!");            
            return ret;
        }
        
        if (restoredAdmin.isBlocked()) {
            
            ret.setResponseCode(ResponseCode.RESPONSE_CODE_ERROR);
            ret.setHeader("Модерирование");
            ret.setMessage("Администратор с ником " + restoredAdmin.getNick() + " забанен, доступ в админку Chat-X блокирован.");            
            return ret;
        }
        
        ret.setResponseCode(ResponseCode.RESPONSE_CODE_OK);
        ret.setHeader("Аутентификация прошла успешно");
        ret.setMessage("Вы будете перенаправлены в личный кабинет в течение одной секунды");
        
        aCont.setAdmin(restoredAdmin);      
        
        return ret;    
    }
    //****************************************************************************************************************** 
    public Administrator getAdmin(Administrator admin, String pass)
    {
        String sql = String.format (sqlSelectAdmin, admin.getNick(), pass);
        
        List<Administrator> adminList = new ArrayList<> ();
        try (Connection conn = Db._conn();)
        {
            try (Statement st = conn.createStatement();)
            {  
                ResultSet rs = st.executeQuery(sql); 
                
                while (rs.next()) 
                {
                    Administrator newAdmin = new Administrator ();
                    
                    Integer id = rs.getInt("id");
                    Integer blocked = rs.getInt("is_blocked");
                    
                    String  nick       = rs.getString("nick");
                    String  first_name = rs.getString("first_name");
                    String  last_name  = rs.getString("last_name");
                    String  email      = rs.getString("email");
                    
                    
                    newAdmin.setId        (id);
                    newAdmin.setNick      (nick);
                    newAdmin.setFirstName (first_name);
                    newAdmin.setLastName  (last_name);
                    newAdmin.setEmail     (email);
                    newAdmin.setBlocked   (blocked == 0 ? false : true);
                    
                    adminList.add(newAdmin);
                }
            }            
            catch (SQLException ex) {                       
                LOG._err(ex.getMessage());
            }                
        }
        catch (Exception ex) {   
            LOG._err(ex.getMessage());
        } 
        
        int sz = adminList.size();
        
        switch (sz)
        {
            case 0: 
                return null;
                
            case 1: 
                return adminList.get(0);
        }
            
        return null;
    }
    //****************************************************************************************************************** 
}
