package bgu.spl.net.api;

import bgu.spl.net.impl.BGSServer.ConnectionsImpl;

public interface BidiMessagingProtocol<T> {
    void start(int connectionId, ConnectionsImpl connections );
    void process(T message);

    boolean shouldTerminate();
    public void setHandler(ConnectionHandler<T> CH);
}
