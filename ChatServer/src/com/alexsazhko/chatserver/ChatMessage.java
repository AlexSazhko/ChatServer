package com.alexsazhko.chatserver;

public class ChatMessage {
    private long sendTime;
    private String messageContent;
    private String userName;
    private String messageFlag;

    public ChatMessage(){

    }

    public ChatMessage(long paramSendTime, String paramMsgContent, String userName, String messageFlag) {
        this.sendTime = paramSendTime;
        this.messageContent = paramMsgContent;
        this.userName = userName;
        this.messageFlag = messageFlag;
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


    public String getMessageFlag() {
        return messageFlag;
    }

    public void setMessageFlag(String messageFlag) {
        this.messageFlag = messageFlag;
    }
}
