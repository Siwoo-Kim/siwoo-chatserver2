package com.siwoo.repository;

import com.siwoo.common.Client;

import java.util.Set;

public interface ClientRepository {

    Client save(Client client);
    Set<Client> getClients();
    long size();

}
