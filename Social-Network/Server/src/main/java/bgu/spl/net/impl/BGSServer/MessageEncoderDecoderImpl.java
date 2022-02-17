package bgu.spl.net.impl.BGSServer;
import bgu.spl.net.api.ConnectionHandler;
import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.impl.Messages.ClientMessage;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.regex.Pattern;

public class MessageEncoderDecoderImpl implements MessageEncoderDecoder<ClientMessage> {
    private byte[] bytes;
    private int len;
    private int byteCounter;
    private byte[] opCodeBytes;
    private short msgOpCode;
    private boolean coughtOpCode, coughtUserName;
    private ClientMessage cm;
    private String userName;
    boolean afterFinish=false;
    public MessageEncoderDecoderImpl(){
        bytes = new byte[1 << 10]; //start with 1k
        len = 0;
        byteCounter=0;
        opCodeBytes =new byte[2];
        coughtOpCode=false;
        coughtUserName=false;
        msgOpCode=0;
        userName="";
    }
    private String popString() {
        String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
        String s="";
        reset();
        return result.trim();
    }

    private short popShort(){
        short result= ByteBuffer.wrap(opCodeBytes).getShort();
        return result;
    }
    private void reset(){
        len = 0;
        byteCounter=0;
        bytes = new byte[1 << 10];
    }
    @Override
    public ClientMessage decodeNextByte(byte nextByte) {
        pushByte(nextByte);
        String s = "";
        if ((len == 2 && !coughtOpCode)||(nextByte!=0 &&len==3&&msgOpCode==0)) {
            msgOpCode = nextByte;
            popShort();
            cm = new ClientMessage("", msgOpCode);
            cm.setUserName(userName);
            coughtOpCode = msgOpCode > 0;
        }
        //we need to check if the next byte is ;
        if (coughtOpCode) {
            switch (msgOpCode) {
                case 1:
                case 2: {
                    if (nextByte == ';') {
                        s = new String(popString()).trim();
                        String[] temp = s.split(" ");
                        if(temp.length<3){
                            System.out.println("Problem");
                        }else{
                            cm.setUserName(temp[0]);
                            userName=temp[0];
                            cm.AddString(temp[1]);
                            cm.AddString(temp[2]);
                        }
                        coughtOpCode = false;
                        msgOpCode = 0;
                        afterFinish=true;
                        return cm;
                    }
                    break;
                }
                case 6:{
                    if (nextByte == ';') {
                        s = popString();
                        String[] temp = s.split(" ");
                        for (int i = 0; i < temp.length; i++) {
                            cm.AddString(temp[i]);
                        }
                        coughtOpCode = false;
                        msgOpCode = 0;
                        afterFinish=true;
                        return cm;
                    }
                    break;
                }
                case 3:
                case 7: {
                    if (nextByte == ';') {
                        s = popString();
                        coughtOpCode = false;
                        msgOpCode = 0;
                        return cm;
                    }
                    break;
                }
                case 4: {
                    if (nextByte == ';') {
                        s = popString();
                        cm.AddString(s.substring(0, 1));
                        cm.AddString(s.substring(2, s.length() - 2));
                        coughtOpCode = false;
                        msgOpCode = 0;
                        return cm;
                    }
                    break;
                }
                case 5:
                case 12: {
                    if (nextByte == ';') {
                        s = popString();
                        cm.AddString(s);
                        coughtOpCode = false;
                        msgOpCode = 0;
                        return cm;
                    }
                    break;
                }
                case 8: {
                    if (nextByte == ';') {
                        s = popString();
                        String[] temp = s.split(Pattern.quote("|"));
                        for (int i = 0; i < temp.length; i++) {
                            if(temp[i]!="")
                                cm.AddString(temp[i]);
                        }
                        coughtOpCode = false;
                        msgOpCode = 0;
                        return cm;
                    }
                    break;
                }
            }
        }
        if(afterFinish){
            popString();
            afterFinish=false;
        }


        return null; //not a line yet
    }



    @Override
    public byte[] encode(ClientMessage message) {
        return (message.getContent() +'\0').getBytes();
    }



    public static byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }

    private void pushByte(byte nextByte) {

            if (len >= bytes.length) {
                bytes = Arrays.copyOf(bytes, len * 2);
            }
            bytes[len++] = nextByte;

    }


}