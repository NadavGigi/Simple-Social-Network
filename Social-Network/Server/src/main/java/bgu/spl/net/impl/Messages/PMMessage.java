package bgu.spl.net.impl.Messages;

import bgu.spl.net.api.Message;
import bgu.spl.net.srv.DataBase;

import java.util.Vector;

public class PMMessage extends Message {
    private String date;
    private String whotoSend;
    public PMMessage(String _userName,String _whotoSend,String _content,String _date) {
        super((short)6);
        userName=_userName;
        content=_content;
        date=_date;
        whotoSend=_whotoSend;
    }
    public String response() {
        if(db.AddPM(this))
            return "ACK 6 "+getContent();
        return "ERROR 6 ";
    }



    public String getUserName() {
        return userName;
    }

    public String getDate() {
        return date;
    }

    public String getWhotoSend() {
        return whotoSend;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }



    public void setDate(String date) {
        this.date = date;
    }

    public void setWhotoSend(String whotoSend) {
        this.whotoSend = whotoSend;
    }
}
