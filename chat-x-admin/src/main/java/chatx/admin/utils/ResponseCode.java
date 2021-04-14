package chatx.admin.utils;

public enum ResponseCode {
    
    RESPONSE_CODE_UNDEFINED ("UNDEFINED"),
    
    RESPONSE_CODE_OK ("OK"),
    RESPONSE_CODE_ERROR ("ERROR"),
    RESPONSE_CODE_WARNING ("WARNING");
        
    private String title;

    ResponseCode (String title) {
       this.title = title;
   }

    public String getTitle() {
       return title;
    }

    @Override
    public String toString() {
       return "ResponseCode {" + "title='" + title + '\'' + '}';
    }
    
    public String getVal() {
       return title;
    }
    
    public int getInt () {
        
        switch (title)
        {
            case "UNDEFINED":  return 0;
                    
            case "OK": return 1;
            case "ERROR": return 2;
            case "WARNING": return 3;
            
        }
       return 0;
    }
}
