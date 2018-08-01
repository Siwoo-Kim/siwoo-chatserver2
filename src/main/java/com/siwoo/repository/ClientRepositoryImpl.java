package com.siwoo.repository;

import com.siwoo.common.Client;

import java.util.*;

public class ClientRepositoryImpl implements ClientRepository {
    private static final Set<Client> database;
    private static long SEQUENCE = 1;
    static {
        database = Collections.synchronizedSet(new HashSet<>());
    }

    synchronized long nextId() {
        return SEQUENCE++;
    }

    @Override
    public Client save(Client client) {
        if(!database.contains(client)) {
            Client _client = new Client.Builder()
                    .id(nextId())
                    .name(client.getName())
                    .address(client.getAddress())
                    .build();
            database.add(_client);
            System.out.println("New client "+ client.getName() +":ID"+ client.getId() + "is added");
            return _client;
        } else {
            return client;
        }
    }

    @Override
    public Set<Client> getClients() {
        return new HashSet<>(database);
    }

    @Override
    public long size() {
        return database.size();
    }
}
