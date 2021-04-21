package chatx.serv.web.controller;

import org.json.simple.JSONObject;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


// *********************************************************************************************************************
@RestController
public class VersionController 
{
    @RequestMapping(value = {"/version", "/version/"},  
                    method = RequestMethod.GET,
                    produces = "application/json;charset=utf-8")
    
    public  String showPgUserReg (final Model model)
    {
        String version = "0.5";
        String date = "2021.04.21"; 
        
        JSONObject jRet = new JSONObject();
        jRet. put ("version", version);
        jRet. put ("date", date);
    
        String json = jRet.toJSONString();
        
        return json;
    }        
     // ***************************************************************************************************************
}
