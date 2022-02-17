#ifndef CONNECTION_HANDLER__
#define CONNECTION_HANDLER__

#include <string>
#include <iostream>
#include <boost/asio.hpp>
#include <mutex>
#include <condition_variable>

using boost::asio::ip::tcp;

class ConnectionHandler {
private:
    const std::string host_;
    const short port_;
    boost::asio::io_service io_service_;
    tcp::socket socket_;
    bool isLoggedOut;

    bool sendFrameAscii(const std::string& frame, char delimiter);


    bool getFrameAscii(std::string &frame, char delimiter);
    bool getBytes(char bytes[], unsigned int bytesToRead);


public:
    ConnectionHandler(std::string host, short port);
    virtual ~ConnectionHandler();

    bool connect();

    bool getLine(std::string& frame);


    bool sendLine(std::string& line);

    void close();

    bool sendBytes(const char bytes[], int bytesToWrite);
}; //class ConnectionHandler

#endif