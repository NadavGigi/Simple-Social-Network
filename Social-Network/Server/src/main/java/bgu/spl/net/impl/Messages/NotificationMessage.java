package bgu.spl.net.impl.Messages;

import bgu.spl.net.api.Message;
import bgu.spl.net.srv.DataBase;

import java.util.Vector;

public class NotificationMessage extends Message {
    private Message msg;
    private int type;
    public NotificationMessage(PMMessage _msg){
        super((short)9);
        msg=_msg;
        userName=msg.getUserName();
        content=msg.getContent();
        if(msg.getClass()==PMMessage.class)
            type=0;
        else
            type=1;
    }
    public String response() {
        if(type==0)
             return "PM "+userName+" "+content;
        return "Public "+userName+" "+content;
    }

    public String getUserName() {
        return userName;
    }






}
