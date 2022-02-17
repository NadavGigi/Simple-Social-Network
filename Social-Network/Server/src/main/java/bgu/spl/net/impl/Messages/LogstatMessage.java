package bgu.spl.net.impl.Messages;

import bgu.spl.net.api.Message;
import bgu.spl.net.srv.DataBase;

public class LogstatMessage extends Message {
    public LogstatMessage(String _userName) {
        super((short)7);
        userName=_userName;
    }
    public String response() {
        if(db.LogStat(this)){
            return "ACK 7 "+content;
        }
        return "ERROR 7 ";
    }




}
