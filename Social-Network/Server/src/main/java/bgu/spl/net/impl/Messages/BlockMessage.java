package bgu.spl.net.impl.Messages;

import bgu.spl.net.api.Message;

public class BlockMessage extends Message {
    private String userToBlock;
    public BlockMessage(String _userName,String _userToBlock){
        super((short)12);
        userName=_userName;
        userToBlock=_userToBlock;
    }



    public String getUserToBlock() {
        return userToBlock;
    }

    public String response() {
        if(db.Block(this))
            return "ACK 12 BLOCK "+userToBlock;
        return "ERROR 12";
    }
}
