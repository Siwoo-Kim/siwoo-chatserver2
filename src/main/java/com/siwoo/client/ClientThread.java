package com.siwoo.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientThread  {
    private Socket socket;
    private DataInputStream request;
    private DataOutputStream response;
    private String name;
    private Runnable reciever = () -> {
        while (request != null) {
            try {
                System.out.println(request.readUTF());
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    private Runnable sender = () -> {
        Scanner scanner = new Scanner(System.in);
        try {
            if(response != null) {
                response.writeUTF(name);
            }
            while (response != null) {
                String message = scanner.nextLine();
                response.writeUTF("[" + name + "] " + message);
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
    };

    public ClientThread(Socket socket, String name) {
        this.socket = socket;
        this.name = name;
        try {
            request = new DataInputStream(socket.getInputStream());
            response = new DataOutputStream(socket.getOutputStream());
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        new Thread(sender).start();
        new Thread(reciever).start();
    }
}
