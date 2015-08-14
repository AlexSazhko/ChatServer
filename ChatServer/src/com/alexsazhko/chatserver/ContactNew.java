package com.alexsazhko.chatserver;


public class ContactNew{

    private String messageFlag;
    private Contact contact;


    public ContactNew(){
        messageFlag = MessageState.NEW.name();
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }


}
