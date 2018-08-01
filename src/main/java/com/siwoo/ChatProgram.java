package com.siwoo;

import com.siwoo.repository.ClientRepository;
import com.siwoo.repository.ClientRepositoryImpl;
import com.siwoo.server.ChatServer;

public class ChatProgram {

    public static void main(String[] args) {
        ClientRepository clientRepository = new ClientRepositoryImpl();
        ChatServer chatServer = new ChatServer(clientRepository);
        chatServer.start();
    }

}
