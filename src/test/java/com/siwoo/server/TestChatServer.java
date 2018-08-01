package com.siwoo.server;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestChatServer {

    @Test
    public void properties() {
        assertTrue(ChatServer.getHOSTNAME().equals("localhost"));
        assertTrue(ChatServer.getPORT() == 8080);
    }
}
