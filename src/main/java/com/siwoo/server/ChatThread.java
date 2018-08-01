package com.siwoo.server;

import com.siwoo.common.Client;
import com.siwoo.repository.ClientRepository;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static com.siwoo.server.Coloring.ANSI_PURPLE;

public class ChatThread implements Runnable {
    Socket socket;
    DataInputStream request;
    DataOutputStream response;
    ChatServer chatServer;
    Client client;
    ClientRepository clientRepository;

    public ChatThread(Socket socket, ChatServer chatServer, ClientRepository repository) {
        this.socket = socket;
        this.chatServer = chatServer;
        this.clientRepository = repository;
        try {
            this.request = new DataInputStream(socket.getInputStream());
            this.response = new DataOutputStream(socket.getOutputStream());
        }catch (IOException e) {
            System.out.println("ChatThread got error " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String name = request.readUTF();
            client = chatServer.addChatter(new Client.Builder()
                    .name(name)
                    .address(socket.getInetAddress())
                    .build(), response);

            chatServer.emit(ANSI_PURPLE + "* " + name +":ID " + client.getId() + " join the chatting program", client);
            System.out.println(ANSI_PURPLE + "Current number of joined client :" + chatServer.currentChatterSize());
            System.out.println(ANSI_PURPLE + "Total number of joined client : " + chatServer.totalChatterSize());

            while (request != null) {
                chatServer.emit(request.readUTF(), client);
            }
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            chatServer.leftChat(client, response);
            System.out.println(ANSI_PURPLE + "& " + client.getName()+":ID "+client.getId()+" left chatting");
        }
    }
}
