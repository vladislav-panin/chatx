package chatx.serv.web.controller;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


// *********************************************************************************************************************
@Controller
public class VersionController 
{
    @RequestMapping (value = {"/version", "/version/"},  
                     method = RequestMethod.GET,
                     produces = "application/json;charset=utf-8")   
    
    public JSONObject showPgUserReg (final Model model) 
    {
        String version = "0.5";
        String date = "2021.04.21"; 
        
        JSONObject jRet = new JSONObject();
        jRet. put ("version", version);
        jRet. put ("date", date);
         
        return jRet;
    }        
     // ***************************************************************************************************************
}
