package bgu.spl.net.impl.Messages;

import bgu.spl.net.api.Message;

import java.util.Vector;

public class ClientMessage extends Message {
    Vector<String> values;
    public ClientMessage(String data,short _opCode){
        super(_opCode);
        content=data;
        values=new Vector<String>();
    }
    public ClientMessage(){
        super((short)0);
    }
    public String response() {
        return "";
    }
    public void AddString(String s){
        values.add(s);
    }

    public Vector<String> getValues() {
        return values;
    }
}