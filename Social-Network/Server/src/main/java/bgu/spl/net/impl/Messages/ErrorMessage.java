package bgu.spl.net.impl.Messages;

import bgu.spl.net.api.Message;

public class ErrorMessage extends Message {
    private Message msg;
    private short Mopcode;
    public ErrorMessage(Message _msg){
        super((short)10);
        msg=_msg;
        Mopcode=msg.getOpCode();
    }


    public String response() {
        return "ERROR "+Mopcode;
    }
}
