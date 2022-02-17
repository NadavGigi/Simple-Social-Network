package bgu.spl.net.api;

public interface Connections<T> {
    //sends a message T to client represented by
    //the given connId
    boolean send(int connId,T msg);
    //sends a message T to all active clients. This includes
    //clients that has not yet completed log-in by the BGS protocol. Remember,
    //Connections belongs to the server pattern implemenration, not the protocol!
    void broadcast(T msg);
    //removes active client connId from map
    void disconnect(int connId);
}
