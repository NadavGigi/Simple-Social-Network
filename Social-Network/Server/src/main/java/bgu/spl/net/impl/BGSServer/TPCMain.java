package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.impl.ThreadPerClientServer;

public class TPCMain {
    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
       new ThreadPerClientServer(port,
               () -> new BidiMessagingProtocolImpl(),
               () -> new MessageEncoderDecoderImpl()).serve();

   }
}
