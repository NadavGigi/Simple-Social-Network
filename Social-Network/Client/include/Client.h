#ifndef CLIENT_CLIENT_H
#define CLIENT_CLIENT_H
#include <thread>
#include "ConnectionHandler.h"

class Client{
private:
    ConnectionHandler connectionHandler;
    bool stop;
    std::string clientName;
public:
    Client(std::string host, short port);
    //Client & operator=(const Client & other); //copy assignment
    Client(const Client & other); //copy constructor
    ~Client(); //destructor
    //Client(Client && other); //Move constructor
    //Client & operator=(Client && other); //Move assignment
    void runWriter();
    void runReader();
    ConnectionHandler& getConnectionHandler();
    bool getStop();
    void fromShortToBytes(short, char*);

};

#endif //CLIENT_CLIENT_H
