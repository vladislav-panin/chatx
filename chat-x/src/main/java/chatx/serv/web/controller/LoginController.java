package chatx.serv.web.controller;

import chatx.serv.entities.ChatUserContainer;
import chatx.serv.entities.ChatUser;
import chatx.serv.utils.ResponseCode;
import chatx.serv.web.sevice.LoginService;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/* функциональность по задаче Task #8420, уже реализована.
   в рамках освоения гита будем считать что мы сделали это на ветке: task_8420_authentification_page*/
// *********************************************************************************************************************
@Controller
public class LoginController 
{
    // *****************************************************************************************************************
    @RequestMapping (value = {"/", "/login", "/index.html", "/login/index.html"},  
                     method = RequestMethod.GET,
                     produces = {"text/html;charset=utf-8"})
    
    public String showIndex () 
    {  
        return "login";
    }
    // *****************************************************************************************************************
    
    @RequestMapping (value = {"/do-chat-login",  "/do-chat-login/"}, 
                     method = RequestMethod.POST,                      
                     produces = "text/html;charset=utf-8")
    
    public String doLogin ( final Model model,
                            HttpSession session,
                            
                            
        /*  Если вдруг сервер отвечает на запрос урла статусом 400 - то скорее всего имя, 
            указанное как @RequestParam(name = "pass"), в форме html имеет аттрибут "name" с ошибкой.
            В случае с полем ввода "pass", там было <input ... name="passW"/>
            При добавлении в @RequestParam необязательности поля ("required = false") - значение "pass" будет null в таком случае. */            
                            @RequestParam(required = false, name = "nick") String nick,
                            @RequestParam(required = false, name = "pass") String pass)             
    {   
        ChatUser  user = new ChatUser (nick);
        ChatUserContainer uCont = new ChatUserContainer (user);
        
        AjaxMsg amsg = (new LoginService ()).login(uCont, pass);        
        
        ResponseCode rc = amsg.getResponseCode();
        
        if (rc == ResponseCode.RESPONSE_CODE_OK) {
        
            session.setAttribute("user_id", uCont.getUser().getId());
            session.setAttribute("loggedIn", rc.toString());            
        
            return "redirect:/personal-area";            
        }
        
        String loginMessage = amsg.getMessage(); 
        
        model.addAttribute("msgPanelStyle", " display:block; color: red; min-height: 24px; padding: 10px 16px 10px 16px; border-color: red; ");
        model.addAttribute("loginMessage", loginMessage);   
        
        return "login";        
    }
}
