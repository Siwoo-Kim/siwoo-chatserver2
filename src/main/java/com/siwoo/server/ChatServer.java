package com.siwoo.server;

import com.siwoo.common.Client;
import com.siwoo.repository.ClientRepository;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.siwoo.server.Coloring.*;

public class ChatServer {
    public static final int PORT;
    public static final String HOSTNAME;
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:MM:ss.SSS");
    private Map<Client, OutputStream> chatters;
    private ClientRepository clientRepository;

    static {
        Properties properties = new Properties();
        try(InputStream in = new FileInputStream(Paths.get(".","src/main/resources/server.properties").toFile())){
           properties.load(in);
        } catch (IOException e) {
            System.out.println("Cannot load server properties");
            System.exit(-1);
        }

        PORT = Integer.parseInt(properties.getProperty("server.port"));
        HOSTNAME = properties.getProperty("server.hostname");
    }

    public ChatServer(ClientRepository clientRepository) {
        chatters = Collections.synchronizedMap(new HashMap<>());
        this.clientRepository = clientRepository;
    }

    public void start() {
        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(PORT);
            LocalDateTime startDate = LocalDateTime.now();
            System.out.println(ANSI_PURPLE+ "== Server starts at " + timeFormatter.format(startDate) + " == ");

            while (true) {
                socket = serverSocket.accept();
                System.out.println(ANSI_PURPLE + "[" + socket.getInetAddress() +":" + socket.getPort()+"] accesses to the server");
                ChatThread chatThread = new ChatThread(socket, this, clientRepository);
                new Thread(chatThread).start();
            }
        }catch (IOException e) {
            System.out.println(ANSI_RED + "Server got serious problem. Server down");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static int getPORT() {
        return PORT;
    }

    public static String getHOSTNAME() {
        return HOSTNAME;
    }

    public void emit(String message, Client client) {
        chatters.entrySet()
                .stream()
                .forEach(entrySet -> {
                    try {

                        DataOutputStream response = (DataOutputStream) chatters.get(entrySet.getKey());
                        System.out.println(message);
                        if(entrySet.getKey().equals(client)) {
                            response.writeUTF(ANSI_YELLOW + message);
                        } else {
                            response.writeUTF(ANSI_GREEN + message);
                        }
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    public Client addChatter(Client client, OutputStream response) {
        client = clientRepository.save(client);
        chatters.put(client, response);
        return client;
    }

    public long currentChatterSize() {
        return chatters.keySet().size();
    }

    public long totalChatterSize() {
        return clientRepository.size();
    }

    public void leftChat(Client client, DataOutputStream response) {
        chatters.remove(client);
    }
}
