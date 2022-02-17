package bgu.spl.net.impl.BGSServer;


import bgu.spl.net.api.Server;
import bgu.spl.net.srv.DataBase;

public class ReactorMain {
    public static void main(String[] args) throws Exception{

        int port=7777;
        int threadsAmount=5;
        if(args.length>1){
            port=Integer.parseInt(args[0]);
            threadsAmount=Integer.parseInt(args[1]);
        }
        else throw new Exception("not enough arguments!");
        Server.reactor(
                threadsAmount,
                port, //port
                () -> new BidiMessagingProtocolImpl() {
                }, //protocol factory
                MessageEncoderDecoderImpl::new //message encoder decoder factory
        ).serve();
    }
}