package com.alexsazhko.chatserver;


import java.util.ArrayList;
import java.util.List;

public class Contact{
    private int id;
    private String name = null;
    private boolean isConnected = false;
    private String pictureName;
    private List<ChatMessage> messagesList;

    public Contact(){
        messagesList = new ArrayList<ChatMessage>();
    }

    public Contact(int id, String name){
        this.name = name;
        this.id = id;
        messagesList = new ArrayList<ChatMessage>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setIsConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public List<ChatMessage> getMessagesList() {
        return messagesList;
    }

    public void setMessagesList(List<ChatMessage> messagesList) {
        this.messagesList = messagesList;
    }

}
