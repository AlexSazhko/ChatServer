package com.alexsazhko.chatserver;


import java.util.ArrayList;

public class ContactList{

    private String messageFlag;
    private ArrayList<Contact> clients;

    public ArrayList<Contact> getClients() {
        return clients;
    }

    public void setClients(ArrayList<Contact> clients) {
        this.clients = clients;
    }

    

    public ContactList(){
        messageFlag = MessageState.SEARCH.name();
        clients = new ArrayList<Contact>();
    }


}
