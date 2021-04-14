package chatx.serv.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//******************************************************************************
@Controller
public class ChatController
{
    //**************************************************************************    
    @RequestMapping (value = {"/chat/", "/chat"},  
                     method = RequestMethod.GET,
                     produces = "text/html;charset=utf-8")    
    
    public String showPgChat () 
    {  
        return "chat";
    }
        
    //**************************************************************************    
}
