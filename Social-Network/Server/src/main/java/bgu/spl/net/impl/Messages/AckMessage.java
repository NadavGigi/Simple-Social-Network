package bgu.spl.net.impl.Messages;

import bgu.spl.net.api.Message;

public class AckMessage extends Message {
    private short opcode;
    private Message msg;
    public AckMessage(Message _msg){
        super((short)10);
        msg=_msg;
        opcode=msg.getOpCode();
    }
    public Message getMessage(){
        return msg;
    }



    public String response() {
        return " "+opcode+" "+content+" ";
    }
}
