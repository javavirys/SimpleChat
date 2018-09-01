package com.example.javavirys.socketiochat.list.models;

public class Message {
    private int gravity;
    private String name;
    private String msg;
    private long time;
    private String mail;

    public Message() { }

    public Message(String mail, String name, String msg, long time, int gravity) {
        this.mail = mail;
        this.name = name;
        this.msg = msg;
        this.time = time;
        this.gravity = gravity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
