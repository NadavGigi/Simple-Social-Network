#include <thread>
#include "../include/ConnectionHandler.h"
#include "../include/Client.h"
#include <boost/asio.hpp>
#include <algorithm>
#include <string>

Client::Client(std::string host, short port): connectionHandler(host, port), stop(false){}



void Client::runWriter() {
    std::map<std::string, int> map;
        map["REGISTER"] = 1;
        map["LOGIN"] = 2;
        map["LOGOUT"] = 3;
        map["FOLLOW"] = 4;
        map["POST"] =5;
        map["PM"]=6;
        map["LOGSTAT"] =7;
        map["STAT"]=8;
    while (!this->stop) {
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
        std::string line(buf);
        int endOfOpCOde = line.find(' ');
        std::string opCode = line.substr(0, endOfOpCOde);
        line = line.substr(endOfOpCOde + 1);
        char* bytes=new char[2];
        int opcode = map[opCode];
        std::string sendThis="";
        fromShortToBytes(opcode,bytes);
        sendThis.append(1,bytes[0]);
        sendThis.append(1,bytes[1]);
        line = sendThis+line+" ;";
        if(!connectionHandler.sendLine(line)){
            std::cout<<"Error"<<std::endl;
            break;
        }

    }
}
/**
 * The method that the thread threadRead is responsible for.
 */
void Client::runReader(){
    while(!this->stop) {
        std::string answer;
        if (!connectionHandler.getLine(answer)) {
            this->stop=true;
            break;
        }

        if (answer != "") {
            std::cout << answer.substr(0,answer.length()-2) << std::endl;
            std::string::size_type index(answer.find('>', 0));
            std::string s = answer.substr(index + 2, index + 6);
            if (answer.rfind("ACK<3>", 0) == 0)  {
                this->getConnectionHandler().close();
                this->stop = true;
            }
        }
    }
    //std::cerr << "terminate run reader" << std::endl;
}

ConnectionHandler& Client::getConnectionHandler(){
    return this->connectionHandler;
}

bool Client::getStop(){
    return this->stop;
}

Client :: ~Client() {
    this->connectionHandler.close();
}

void Client::fromShortToBytes(short num, char* bytesArr){
    bytesArr[0]=((num>>8)&0xFF);
    bytesArr[1] = (num % 0xFF);
}
