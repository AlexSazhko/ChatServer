package com.alexsazhko.chatserver;

public class ChatMessage {
    private long sendTime;
    private String messageContent;
    private String userName;
    private String toUserName;
    private String messageFlag; //NEW, MESSAGE, END
    
    private boolean ownMessage = false;

    public ChatMessage(){

    }

    public ChatMessage(long paramSendTime, String paramMsgContent, String userName, String messageFlag, boolean ownMessage) {
        this.sendTime = paramSendTime;
        this.messageContent = paramMsgContent;
        this.userName = userName;
        this.messageFlag = messageFlag;
        this.ownMessage = ownMessage;
    }

    public long getSendTime() {
        return sendTime;
    }

    public String getMsgContent() {
        return messageContent;
    }

    public void setMsgContent(String paramMsgContent) {
    	messageContent = paramMsgContent;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getMessageFlag() {
        return messageFlag;
    }

    public void setMessageFlag(String messageFlag) {
        this.messageFlag = messageFlag;
    }
    
    public boolean isOwnMessage() {
        return ownMessage;
    }

    public void setOwnMessage(boolean ownMessage) {
        this.ownMessage = ownMessage;
    }
}
