#include <stdlib.h>
#include "../include/ConnectionHandler.h"
#include "../include/Client.h"
#include <thread>

int main (int argc, char **argv) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[1] << " host port" << std::endl << std::endl;
        return -1;
    }

    std::string host = argv[1];
    short port = atoi(argv[2]);

    Client client(host, port);

    if (!client.getConnectionHandler().connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }

    std::thread threadWrite (&Client::runWriter, &client);
    std::thread threadRead (&Client::runReader, &client);

    threadRead.join();
    threadWrite.join();
    if (client.getStop()) {
        client.getConnectionHandler().close();
    }

    return 0;
}