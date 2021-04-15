package chatx.admin.web.controller;

import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class PersonalAreaController
{   
    //******************************************************************************************************************
    @RequestMapping (value = "/personal-area",  
                     method = RequestMethod.GET,                      
                     produces = "text/html;charset=utf-8")    
    
    public String showAdminLogin (final Model model, HttpSession session) 
    {       
        return "personal-area";
    }      
    //******************************************************************************************************************
}
