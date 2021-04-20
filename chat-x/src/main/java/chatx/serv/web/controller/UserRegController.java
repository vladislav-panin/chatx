package chatx.serv.web.controller;

import chatx.serv.web.sevice.RegisterService;
import chatx.serv.entities.ChatUser;
import chatx.serv.entities.ChatUserContainer;
import chatx.serv.utils.ResponseCode;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

// *********************************************************************************************************************
@Controller
public class UserRegController 
{
    private static AtomicInteger name_counter = new AtomicInteger (0);     
    // *****************************************************************************************************************
    @RequestMapping (value = {"/user-reg", "/user-reg/"},  
                     method = RequestMethod.GET,
                     produces = "text/html;charset=utf-8")   
    
    public String showPgUserReg (final Model model) 
    {                
        return "user-reg";
    }        
     // ****************************************************************************************************************
    
    @RequestMapping (value = {"/do-user-registration", "/do-user-registration/"},  
                     method = RequestMethod.POST,                      
                     produces = "text/html;charset=utf-8")
    
    public String doRegisterUser ( final Model model,
                            HttpSession  session,
                            @RequestParam(required = false, name = "nick") String nick,
                            @RequestParam(required = false, name = "pass") String pass,
                            @RequestParam(required = false, name = "pass_check") String pass_check,
                            @RequestParam(required = false, name = "email") String email,
                            @RequestParam(required = false, name = "firstname") String firstname,
                            @RequestParam(required = false, name = "lastname") String lastname) throws SQLException
    {  
        if (null != nick)
            nick = nick.trim();
        
        if (null != firstname)
            firstname = firstname.trim();
        
        if (null != lastname)
            lastname = lastname.trim();
        
        AjaxMsg amsg = new AjaxMsg ();
        checkParams (amsg, nick, pass, pass_check, email, firstname, lastname);
        
        ResponseCode rc = amsg.getResponseCode();
        if (rc != ResponseCode.RESPONSE_CODE_UNDEFINED) {
            
            doErrorResponse (model, amsg);
            return "user-reg";
        }
                
        ChatUser  user = new ChatUser (-1, nick, firstname, lastname, email);                  
        ChatUserContainer uCont = new ChatUserContainer (user);
        
        amsg = (new RegisterService ()).register (uCont, pass);
        if (amsg.getResponseCode() == ResponseCode.RESPONSE_CODE_OK) {
        
            session.setAttribute("user_id", user.getId());
            session.setAttribute("loggedIn", rc.toString());            
        
            return "redirect:/personal-area";            
        }
        
        doErrorResponse (model, amsg);        
        return "user-reg";        
    }
    // *****************************************************************************************************************
    
    public void doErrorResponse (final Model model, AjaxMsg amsg)
    {
        String regMessage = amsg.getMessage();
        
        model.addAttribute("msgPanelStyle", " display:block; color: red; min-height: 24px; padding: 10px 16px 10px 16px; border-color: red; ");
        model.addAttribute("regMessage", "ОШИБКА: " + regMessage);
    }
    // *****************************************************************************************************************
    public void checkParams (  AjaxMsg amsg
                             , String nick 
                             , String pass
                             , String pass_check
                             , String email
                             , String firstname
                             , String lastname)
    {
        if (null == nick || nick.isEmpty()) {
            
            amsg.setHeader("Регистрация невозможна");
            amsg.setMessage("Ник не может быть пустым");
            amsg.setResponseCode(ResponseCode.RESPONSE_CODE_ERROR);
            return;
        }
        
        if (null == pass || pass.isEmpty()) {
            
            amsg.setHeader("Регистрация невозможна");
            amsg.setMessage("Пароль не может быть пустым");
            amsg.setResponseCode(ResponseCode.RESPONSE_CODE_ERROR);
            return;
        }
        
        if (null == pass_check || pass_check.isEmpty()) {
            
            amsg.setHeader("Регистрация невозможна");
            amsg.setMessage("Пароли не совпадают");
            amsg.setResponseCode(ResponseCode.RESPONSE_CODE_ERROR);
            return;
        }
        
        if (!pass_check. equals (pass)) {
            
            amsg.setHeader("Регистрация невозможна");
            amsg.setMessage("Пароли не совпадают");
            amsg.setResponseCode(ResponseCode.RESPONSE_CODE_ERROR);
            return;
        }
            
        if (null == email || email.isEmpty()) {
            
            amsg.setHeader("Регистрация невозможна");
            amsg.setMessage("email не может быть пустым");
            amsg.setResponseCode(ResponseCode.RESPONSE_CODE_ERROR);
            return;
        }
        
        if (null == firstname || firstname.isEmpty()) {
            
            amsg.setHeader("Регистрация невозможна");
            amsg.setMessage("Поле 'Имя' не может быть пустым");
            amsg.setResponseCode(ResponseCode.RESPONSE_CODE_ERROR);
            return;
        }
        
        if (null == lastname || lastname.isEmpty()) {
            
            amsg.setHeader("Регистрация невозможна");
            amsg.setMessage("Поле 'Фамилия' не может быть пустым");
            amsg.setResponseCode(ResponseCode.RESPONSE_CODE_ERROR);
            return;
        }
    }
}
