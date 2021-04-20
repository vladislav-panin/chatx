package chatx.serv.web.controller;

import chatx.serv.entities.ChatUser;
import chatx.serv.utils.ResponseCode;
import chatx.serv.web.sevice.PersonalAreaService;
import java.sql.SQLException;
import javax.naming.AuthenticationException;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

// *********************************************************************************************************************
@Controller
public class PersonalAreaController 
{
    // *****************************************************************************************************************
    @RequestMapping (value = {"/personal-area/", "/personal-area"},  
                     method = RequestMethod.GET,
                     produces = "text/html;charset=utf-8")    
    
    public String showPgPersonalArea (final Model model, HttpSession  session) throws AuthenticationException 
    {  
        if (null == session) {       
            String err = "ERROR! Пользователь без сессии не должен получить доступ к этой странице!";
            System.out.println(err);
            throw new AuthenticationException (err);
        }

        Long user_id = (Long)session.getAttribute ("user_id");
        
        if (null == user_id) {       
            String err = "ERROR! Попытка входа со старой сессией - пользователь имеет сессию, но в сессии нет идентификатора пользователя!";
            System.out.println(err);
            throw new AuthenticationException (err);
        }
        
        if (-1L == user_id) {       
            String err = "ERROR! Попытка входа без user_id (=-1L)!";
            System.out.println(err);
            throw new AuthenticationException (err);
        }
        
        setDataToModel (model, user_id);
        
        return "personal-area";
    }        
    // *****************************************************************************************************************
    
    @RequestMapping (value = {"/personal-area-save/", "/personal-area-seve"},  
                     method = RequestMethod.POST,
                     produces = "text/html;charset=utf-8")    
    
    public String savePgPersonalArea ( final Model model,
            
                            HttpSession  session,
                            @RequestParam(required = false, name = "nick") String nick,
                            @RequestParam(required = false, name = "email") String email,
                            @RequestParam(required = false, name = "firstname") String firstname,
                            @RequestParam(required = false, name = "lastname") String lastname) throws SQLException
    {  
        return "personal-area";
    }   
    // *****************************************************************************************************************
    
    public void setDataToModel (final Model model, Long user_id)
    {
        AjaxMsg amsg = new AjaxMsg ();        
        ChatUser user = (new PersonalAreaService()).getUserRegistrationData (user_id, amsg);
        
        model.addAttribute("nick", user.getNick());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("firstname", user.getFirstName());
        model.addAttribute("lastname", user.getLastName());
        
        if (amsg.getResponseCode() == ResponseCode.RESPONSE_CODE_OK)
            return;
               
        model.addAttribute("msgPanelStyle", " display:block; color: red; min-height: 24px; padding: 10px 16px 10px 16px; border-color: red; ");
        model.addAttribute("regMessage", "ОШИБКА: " + amsg.getMessage());
    }    
}
