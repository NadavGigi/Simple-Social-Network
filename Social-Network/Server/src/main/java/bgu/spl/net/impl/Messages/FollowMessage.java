package bgu.spl.net.impl.Messages;

import bgu.spl.net.api.Message;
import bgu.spl.net.srv.DataBase;

public class FollowMessage extends Message {
    private String ToFollow;
    private int FollowOrUnfollow;//1 means follow and 0 means unfollow
    public FollowMessage(String _userName,int _FollowOrUnfollow,String _ToFollow) {
        super((short)4);
        userName=_userName;
        FollowOrUnfollow=_FollowOrUnfollow;
        ToFollow=_ToFollow;
    }
    public String response() {
        if(FollowOrUnfollow==1) {//follow
            if (db.Follow(userName, ToFollow))
                return "ACK 4 FOLLOW "+FollowOrUnfollow+" "+userName;
        }else{
            if (db.unFollow(userName, ToFollow))
                return "ACK 4 UNFOLLOW "+FollowOrUnfollow+" "+userName;
        }
        return "ERROR 4 ";
    }
}
