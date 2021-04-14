package chatx.admin.web.controller;

import chatx.admin.entities.AdminContainer;
import chatx.admin.entities.Administrator;
import chatx.admin.utils.LOG;
import chatx.admin.utils.ResponseCode;
import chatx.admin.web.sevice.LoginService;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//******************************************************************************
@Controller
public class LoginController 
{
    // *****************************************************************************************************************
    public static boolean checkIfLoginUri (String uri) {        
        
        if (uri.equalsIgnoreCase("/")           || 
            uri.equalsIgnoreCase("/login")      ||  
            uri.equalsIgnoreCase("/login/")     ||  
            uri.equalsIgnoreCase("/index.html") ||
            uri.equalsIgnoreCase("/login/index.html") ||
            uri.equalsIgnoreCase("/ajax/do-login")    ||
            uri.equalsIgnoreCase("/ajax/do-login/") 
           )
            return true;
        
        return false;
    }    
    // *****************************************************************************************************************
    
    @RequestMapping (value = {"/", "/login", "/index.html", "/login/index.html"},  
                     method = RequestMethod.GET,
                     produces = "text/html;charset=utf-8")    
    
    public String showPgLogin (final Model model) {  
        
        LOG._info("GET page /chatx-admin/login");
        return "login";
    }        
    // *****************************************************************************************************************  
    
    @RequestMapping (value = {"/ajax/do-login"},  
                     method = RequestMethod.POST, 
                     produces = "application/json;charset=utf-8")        
        
    public ResponseEntity<String> doLogin (            
            HttpSession session,
            @RequestBody String body) throws ParseException  {        
        
        LOG._info("AJAX service /chatx-admin/ajax/do-login");
        
        JSONParser pser = new JSONParser ();                
        JSONObject jObj = (JSONObject) pser. parse (body);   
        
        String nick = (String) jObj.get("nick");
        String pass = (String) jObj.get("pass");        
        
        Administrator  admin = new Administrator (nick);
        AdminContainer aCont = new AdminContainer (admin);
        
        AjaxMsg amess = (new LoginService ()).login(aCont, pass);        
        
        ResponseCode rc = amess.getResponseCode();
        
        if (rc == ResponseCode.RESPONSE_CODE_OK) {
        
            session.setAttribute("admin_id", admin.getId());
            session.setAttribute("loggedIn", rc.toString());            
        }
        
        ResponseEntity<String> ret = amess.makeResponse();
        return ret;          
    }     
    // *****************************************************************************************************************
}
