package chatx.admin.utils;

//******************************************************************************
public class Ret
{
    public boolean ret   = false;
    public int     code  = 0;
    public String  msg   = "";
    
    public Ret () {}
    
    public Ret (boolean ret)
    {
        this.ret = ret;
    }
    public Ret (boolean ret, String msg)
    {
        this.ret = ret;
        this.msg = msg;
    }
    public Ret (boolean ret, String msg, int code)
    {
        this.ret = ret;
        this.msg = msg;
        this.code = code;
    }
    //******************************************************************************
    public Ret return_self (String msg)
    {
        this.msg = msg;
        return this;
    }
    //******************************************************************************
    public Object return_null (String msg)
    {
        this.msg = msg;
        return null;
    }
    //******************************************************************************
    public Object return_null (boolean ret, String msg)
    {
        this.ret = ret;
        this.msg = msg;
        return null;
    }
    //******************************************************************************
    public Object return_null (boolean ret, String msg, int code)
    {
        this.ret = ret;
        this.msg = msg;
        this.code = code;
        
        return null;
    }
}
//******************************************************************************