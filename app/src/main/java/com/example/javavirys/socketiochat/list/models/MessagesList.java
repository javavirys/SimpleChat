package com.example.javavirys.socketiochat.list.models;

import com.example.javavirys.socketiochat.list.models.Message;

import java.util.ArrayList;
import java.util.List;

public class MessagesList {

    private List<Message> list = new ArrayList<>();

    public MessagesList() {
    }

    public List<Message> getList() {
        return list;
    }

}
