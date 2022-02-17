package bgu.spl.net.impl.Messages;

import bgu.spl.net.api.BidiMessagingProtocol;
import bgu.spl.net.api.Message;
import bgu.spl.net.srv.BlockingConnectionHandler;
import bgu.spl.net.srv.DataBase;

public class RegisterMessage extends Message {
    private String pass;
    private String Birthday;
    public RegisterMessage(String _userName, String _pass,String _Birthday){
        super((short)1);
        userName = _userName;
        pass = _pass;
        Birthday=_Birthday;
    }
    public String response(){
        if(!db.registerUser(userName,pass,Birthday))
            return "ERROR 1 ";
        return "ACK 1 REGISTER "+userName+" "+pass+" "+Birthday;
    }
}

