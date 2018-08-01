package com.siwoo.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.net.InetAddress;

@Getter @ToString
@EqualsAndHashCode(of = {"id","name"})
public class Client {
    private final Long id;
    private final String name;
    private final InetAddress address;

    public Client(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.address = builder.address;
    }

    public static class Builder {
        private Long id;
        private String name;
        private InetAddress address;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder address(InetAddress address) {
            this.address = address;
            return this;
        }

        public Client build() {
            return new Client(this);
        }
    }
}
