package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.BidiMessagingProtocol;
import bgu.spl.net.api.ConnectionHandler;
import bgu.spl.net.impl.Messages.*;
import bgu.spl.net.srv.BlockingConnectionHandler;
import bgu.spl.net.srv.DataBase;
import bgu.spl.net.srv.NonBlockingConnectionHandler;

import java.io.IOException;
import java.util.Vector;

public class BidiMessagingProtocolImpl implements BidiMessagingProtocol<ClientMessage> {
    private boolean shouldTerminate;
    BlockingConnectionHandler<ClientMessage> blockCH;
    NonBlockingConnectionHandler<ClientMessage> nonblock;
    private ConnectionsImpl connection;
    private int connId;
    private String UserName;
    private boolean isConnected;
    public BidiMessagingProtocolImpl(){
        shouldTerminate=false;
        connId=0;
        nonblock=null;
        blockCH=null;
        UserName="";
        connection=ConnectionsImpl.getInstance();
        isConnected=false;
    }
    @Override
    public void start(int connectionId, ConnectionsImpl connections) {
        connId=connection.connect(getHandler());
        connection=connections;
    }

    @Override
    public void process(ClientMessage msg) {
        DataBase db=DataBase.getInstance();
        if(!isConnected&&!db.isConnect(msg.getUserName())){
            connId=connection.connect(getHandler());
            isConnected=true;
        }
        String respone = "";
        Vector<String> vec=msg.getValues();
        switch (msg.getOpCode()) {
            case 1: {
                respone = new RegisterMessage(msg.getUserName(),vec.get(0),vec.get(1)).response();
                if (!respone.startsWith("ERROR")) {
                    UserName=msg.getUserName();
                    db.addConnection(UserName, connId);
                }
                connection.send(connId, new ClientMessage(respone, msg.getOpCode()));
                break;
            }
            case 2: {
                respone = new LoginMessage(msg.getUserName(),vec.get(0)).response();
                connection.send(connId, new ClientMessage(respone, msg.getOpCode()));
                break;
            }
            case 3: {
                respone = new LogoutMessage(msg.getUserName()).response();
                connection.send(connId, new ClientMessage(respone, msg.getOpCode()));
                try {
                    getHandler().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            case 4: {
                respone = new FollowMessage(msg.getUserName(),Integer.parseInt(vec.get(0)),vec.get(1)).response();
                connection.send(connId, new ClientMessage(respone, msg.getOpCode()));
                break;
            }
            case 5: {
                respone = new PostMessage(msg.getUserName(),vec.get(0)).response();
                connection.send(connId, new ClientMessage(respone, msg.getOpCode()));
                break;
            }
            case 6: {
                respone = new PMMessage(msg.getUserName(),vec.get(0),vec.get(1),vec.get(2)).response();
                connection.send(connId, new ClientMessage(respone, msg.getOpCode()));
                break;
            }
            case 7: {
                respone = new LogstatMessage(msg.getUserName()).response();
                connection.send(connId, new ClientMessage(respone, msg.getOpCode()));
                break;
            }
            case 8: {
                respone = new StatMessage(msg.getUserName(), vec).response();
                connection.send(connId, new ClientMessage(respone, msg.getOpCode()));
                break;
            }
            case 9:
            case 11:
            case 10: {
                System.out.println("NOT A POSIBBLE CASE");
                break;
            }
            case 12: {
                respone = new BlockMessage(msg.getUserName(),vec.get(0)).response();
                connection.send(connId, new ClientMessage(respone, msg.getOpCode()));
                break;
            }
        }
    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }
    @Override
    public void setHandler(ConnectionHandler<ClientMessage> blockCH) {//TODO IM NOT SURE ABOUT IT
        if(blockCH.getClass()==BlockingConnectionHandler.class)
            this.blockCH = (BlockingConnectionHandler<ClientMessage>) blockCH;
        else
            this.nonblock = (NonBlockingConnectionHandler<ClientMessage>) blockCH;
    }
    public ConnectionHandler<ClientMessage> getHandler(){
        if(blockCH==null){
            return nonblock;
        }
        return blockCH;
    }

}
