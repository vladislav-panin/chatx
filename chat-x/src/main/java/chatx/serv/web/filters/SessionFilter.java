package chatx.serv.web.filters;

import chatx.serv.utils.ResponseCode;
import chatx.serv.web.controller.LoginController;
import chatx.serv.web.controller.UserRegController;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class SessionFilter implements Filter {
    
    public SessionFilter() {
    }     

    // *****************************************************************************************************************
    @Override
    public void doFilter(ServletRequest request, 
                         ServletResponse response, 
                         FilterChain chain)         throws IOException, ServletException 
    {
        try {
            
                boolean bAccessAllowed = isAccessAllowed (request, response);

                if (bAccessAllowed) {            
                    chain.doFilter(request, response); // даем выполниться всем фильтрам.
                    return;
                }

                // здесь остались запросы неразрешенных ресурсов запрошенных неаутентифицированным пользователем  
                /*
                boolean isHttpStatusSet = response.isCommitted ();
                response.reset();
                isHttpStatusSet = response.isCommitted ();
                ((HttpServletResponse)response).setStatus(HttpServletResponse.SC_NOT_FOUND);
                isHttpStatusSet = response.isCommitted ();
                */
        }
        catch (Exception e)
        {
            String err = e.getMessage();
            if (null != err)
                System.out.println(err);
            else
                e.printStackTrace();
        }
    }    
    // *****************************************************************************************************************
    public boolean isAccessAllowed (ServletRequest request, ServletResponse response) throws IOException 
    {
        boolean isHttpStatusSet = response.isCommitted ();
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        int sts = httpResponse.getStatus();
        
        boolean createNewSession = false;        
        HttpSession session = httpRequest.getSession(createNewSession);
        // ************************************************************
        
        if (null != session) {             
            String loggedinStatus = (String)session.getAttribute("loggedIn");              
            
            if (null != loggedinStatus) {
                
                String rc = ResponseCode.RESPONSE_CODE_OK.toString ();

                if (loggedinStatus.equals(rc))
                    return true;  // пропускаем всех, кто аутентифицирован
            }
        }        
        
        String target = httpRequest.getRequestURI();
        String contextUri = httpRequest.getContextPath();
        
        String uri = target.substring(contextUri.length());
        
        System.out.println("Uri is ^ **** " + uri);
        
        if (LoginController.checkIfLoginUri(uri))
            return true; // пропускаем страницу аутентифиции
        
        if (UserRegController.checkIfRegisterUri(uri))
            return true; // пропускаем страницу регистрации
        
        if (isResourceAllowedOnLogining (uri)) 
            return true; // пропускаем все разрешенные ресурсы
                    
        // здесь остались запросы неразрешенных ресурсов запрошенных не аутентифицированным пользователем 
        // отправляем его логиниться
        httpResponse.sendRedirect(contextUri + "/login");
        
        return false;
    }
    // *****************************************************************************************************************
    public boolean isResourceAllowedOnLogining (String uri) {                
     
        if (uri.endsWith(".js") || 
            uri.endsWith(".css") || 
            uri.endsWith(".gif") || 
            uri.endsWith(".png") || 
            uri.endsWith(".jpg")  
           )
            return true;
        
        return false;
    }
    // *****************************************************************************************************************
    
    @Override
    public void init(FilterConfig arg0) throws ServletException {        
    }

    @Override
    public void destroy() {        
    }    
}
