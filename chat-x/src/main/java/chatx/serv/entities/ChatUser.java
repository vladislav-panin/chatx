package chatx.serv.entities;

public class ChatUser {
    
    protected long   id = 0L;
    protected String nick = null;
    protected String firstName = null;
    protected String lastName = null;
    protected String email = null;
    
    protected boolean blocked = false;
    //******************************************************************************************************************    

    public ChatUser () {
    }
    
    public ChatUser (String nick) {
        this.nick = nick; 
    }
    
    public ChatUser ( long   id, 
                           String nick, 
                           String firstName, 
                           String lastName, 
                           String email
    )
    {
        this.id =        id;
        this.nick =      nick; 
        this.firstName = firstName;
        this.lastName =  lastName;
        this.email =     email;
    }
    //******************************************************************************************************************    
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getNick() {
        return nick;
    }
    public void setNick(String nick) {
        this.nick = nick;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public boolean isBlocked() {
        return blocked;
    }
    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }    
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    //******************************************************************************************************************    
}
