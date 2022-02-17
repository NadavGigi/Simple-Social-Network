package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.ConnectionHandler;
import bgu.spl.net.api.Connections;
import bgu.spl.net.impl.Messages.ClientMessage;

import java.util.HashMap;

public class ConnectionsImpl implements Connections<ClientMessage> {
    private HashMap<Integer,ConnectionHandler<ClientMessage>> conHandVec;
    private int conCounter;
    private static class Holder{
        private static ConnectionsImpl instance = new ConnectionsImpl();
        public static boolean initialized;
    }
    public static ConnectionsImpl getInstance() {
        return ConnectionsImpl.Holder.instance;
    }


    public ConnectionsImpl(){
        this.conHandVec=new HashMap<Integer,ConnectionHandler<ClientMessage>>();
        conCounter=0;
    }
    public synchronized int connect(ConnectionHandler<ClientMessage> ch){//TODO MAYBE DO IT SYNCHRONIZED
        if(!conHandVec.containsKey(ch)){
            conHandVec.put(conCounter,ch);
            conCounter++;

            return conCounter-1;
        }
        return conCounter;

    }

    @Override
    public boolean send(int connId, ClientMessage msg) {
        if(conHandVec.containsKey(connId)){
            msg.setContent(msg.getContent()+" ;");
            conHandVec.get(connId).send(msg);
            return true;
        }
        return false;
    }

    @Override
    public void broadcast(ClientMessage msg) {
        for(ConnectionHandler<ClientMessage> CH : conHandVec.values()){
             CH.send(msg);
        }
    }

    @Override
    public void disconnect(int connId) {
        if (conHandVec.containsKey(connId)) {
            conHandVec.remove(connId);
        }
    }
}
