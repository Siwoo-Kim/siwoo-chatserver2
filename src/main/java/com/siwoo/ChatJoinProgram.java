package com.siwoo;

import com.siwoo.client.ClientThread;
import com.siwoo.server.ChatServer;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import static com.siwoo.server.Coloring.ANSI_YELLOW;

public class ChatJoinProgram {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.printf("Enter your name for chatting: ");
        String name = scanner.next();
        System.out.println("== Just a second, now we are connecting to server... ==");

        try {
            Socket socket = new Socket(ChatServer.HOSTNAME, ChatServer.PORT);
            System.out.println(ANSI_YELLOW + " << Welcome to join the chat program >> ");
            ClientThread clientThread = new ClientThread(socket, name);
            clientThread.start();
        }catch (IOException e) {
            System.out.println("Serious problem happens, the program downs");
            System.exit(-1);
        }
    }
}
