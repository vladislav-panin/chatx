package chatx.serv.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//******************************************************************************
@Controller
public class PrivateChatsListController 
{
    //**************************************************************************    
    @RequestMapping (value = {"/private-chats-list/", "/private-chats-list"},  
                     method = RequestMethod.GET,
                     produces = "text/html;charset=utf-8")    
    
    public String showPgPrivateChatsList () 
    {  
        return "private-chats-list";
    }
        
    //**************************************************************************    
}
