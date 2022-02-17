package bgu.spl.net.api;

import bgu.spl.net.srv.DataBase;

public abstract class Message{
    protected short opCode;
    protected DataBase db;
    protected String content;
    protected String userName;
    public Message(short _opCode){
        opCode=_opCode;
        content="";
        db=DataBase.getInstance();
        userName="";
    }
    public short getOpCode() {
        return opCode;
    }

    public void setOpCode(short opCode) {
        this.opCode = opCode;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
