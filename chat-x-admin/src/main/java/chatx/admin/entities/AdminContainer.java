package chatx.admin.entities;

public class AdminContainer {
    
    protected Administrator admin;

    public AdminContainer (Administrator admin) {
        this.admin = admin;
    }
    
    public Administrator getAdmin() {
        return admin;
    }

    public void setAdmin(Administrator admin) {
        this.admin = admin;
    }
    
}
