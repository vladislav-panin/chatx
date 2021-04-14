package chatx.serv.entities;

public class ChatUserContainer {
    
    protected ChatUser user;

    public ChatUserContainer (ChatUser user) {
        this.user = user;
    }
    
    public ChatUser getUser() {
        return user;
    }

    public void setUser(ChatUser user) {
        this.user = user;
    }
    
}
