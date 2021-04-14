package chatx.serv.web.controller;

import chatx.serv.utils.ResponseCode;
import org.json.simple.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class AjaxMsg
{
    protected String header = ""; 
    protected String message = "";  
    protected ResponseCode responseCode = ResponseCode.RESPONSE_CODE_ERROR;    
    protected HttpStatus httpStatus = HttpStatus.BAD_REQUEST;      
    
    //******************************************************************************************************************    
    public ResponseEntity<String> makeResponse ()
    {
        final HttpHeaders httpHeaders= new HttpHeaders ();                    
        httpHeaders. setContentType (MediaType. APPLICATION_JSON_UTF8);                
        
        String jStr = makeJString ();
        return new ResponseEntity <> (jStr, httpHeaders, httpStatus);
    }
    
    public String makeJString ()
    {
        JSONObject jRet = new JSONObject();
        
        jRet. put ("header", header);
        jRet. put ("responseCode", responseCode.getVal());
        jRet. put ("message", message);
            
        String jStr = jRet.toJSONString();
        return jStr;
    }    
    //******************************************************************************************************************
    
    public void setResponseCode(ResponseCode code) {
        
        responseCode = code;
        switch (code)
        {
            case RESPONSE_CODE_UNDEFINED: 
                httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;   
                break;
                
            case RESPONSE_CODE_OK: 
                httpStatus = HttpStatus.OK;   
                break;
            case RESPONSE_CODE_ERROR: 
                httpStatus = HttpStatus.BAD_REQUEST;   
                break;
            case RESPONSE_CODE_WARNING: 
                httpStatus = HttpStatus.OK;   
                break;            
        }        
    }
    //******************************************************************************************************************
    public ResponseCode getResponseCode() {
        return responseCode;
    }
        
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
    
    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    
    //******************************************************************************************************************
}

