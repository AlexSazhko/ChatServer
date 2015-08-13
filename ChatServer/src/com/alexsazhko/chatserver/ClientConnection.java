package com.alexsazhko.chatserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import com.google.gson.Gson;

public class ClientConnection implements Runnable{
	
	private CallBackMessage callBackMessage;
	private boolean isConnected;
	private Socket clientSocket;
	private DataInputStream dataInputStream = null;
	private DataOutputStream dataOutputStream = null;
	private List<ClientConnection> clientsThread;
	private ClientConnection toClient;
	private String userName;
	private String toUserName;

	public ClientConnection(Socket clientSocket, List<ClientConnection> clientsThread){
		this.clientSocket = clientSocket;
		this.clientsThread = clientsThread;
		isConnected = true;
		toClient = null;
	}

	public void run() {
		ChatMessage chatMessage = null;
		
		try {
            dataInputStream = new DataInputStream(clientSocket.getInputStream());
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

    		while (isConnected) {
	            if(dataInputStream != null && dataInputStream.available() > 0){
	    			String stringMessage = dataInputStream.readUTF();
	    			chatMessage = getChatMessage(stringMessage);
	    			MessageState state = MessageState.valueOf(chatMessage.getMessageFlag());
	    			
	    			switch(state){
	    			case SEARCH:  	
	    				System.out.println("find User " + chatMessage.getMsgContent());
    					synchronized (clientsThread) {
    						String findedName = "";
    						for(ClientConnection findedclient: clientsThread){
    							if(findedclient.userName.equalsIgnoreCase(chatMessage.getMsgContent())){
    								findedName = findedclient.userName;
    							}
    						}			
    						dataOutputStream.writeUTF(setMessageToSend(composeMessage(MessageState.SEARCH, findedName)));
	    					dataOutputStream.flush();
	    				
	    				}
	  
	    				break;
	    				case NEW:  	
	    					synchronized (clientsThread) {
		    					clientsThread.add(this);
		    				}
		    				System.out.println(clientsThread.size());
		    				userName = chatMessage.getUserName();
		    				
		    				callBackMessage.setText("User " + userName + " joined to the chat" + "\n");
		    				break;
	    				case MESSAGE:
	    					toUserName = chatMessage.getToUserName();
	    					callBackMessage.setText(userName + " to " + toUserName + chatMessage.getMsgContent() + "\n");
		    				System.out.println("toUser " + toUserName);
		    				if(toClient == null) {
			    			    synchronized (clientsThread) {
			    			    	toClient = getToClient(toUserName);
			    			    }
			    				toClient.dataOutputStream.writeUTF(stringMessage);
		    					toClient.dataOutputStream.flush(); 
		    				}
		    				else{	
		    					toClient.dataOutputStream.writeUTF(stringMessage);
		    					toClient.dataOutputStream.flush();    			
		    				}
		    				System.out.println("message: " + chatMessage.getMsgContent() +  " sended to " + toClient.userName);
		    				break;
	    				case END:
	    					callBackMessage.setText("User " + chatMessage.getUserName() + " leave the chat" + "\n");
	    					synchronized (clientsThread) {
		    					clientsThread.remove(this);
		    				}
		    				System.out.println(clientsThread.size());
		    				toClient.toClientDisconnected();
		    				
		    				isConnected = false;
		    				break;
	    				    				
	    			}
	            }
	            try {
					Thread.sleep(100);
				} catch (InterruptedException e) {						
					e.printStackTrace();
				}
	    	}
   
            
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            //Close the stream
            if (dataOutputStream != null) {
                try {
                    dataOutputStream.close();
                    } catch (IOException e) {
                    e.printStackTrace();
                    }
                }
            //Close the stream
            if (dataInputStream != null) {
                try {
                    dataInputStream.close();
                    } catch (IOException e) {
                    e.printStackTrace();
                    }
                }
            //Close the socket
            if (clientSocket != null) {
                try {
                	clientSocket.close();
                    } catch (IOException e) {
                    e.printStackTrace();
                    }
                }
            } //End of finally
		
	}
    
	
	public void registerCallBack(CallBackMessage callBackMessage){
		this.callBackMessage = callBackMessage;
	}
	
    private ChatMessage getChatMessage(String json){
        Gson gson = new Gson();
        ChatMessage message = gson.fromJson(json, ChatMessage.class);
        return message;
    }
    
    private ClientConnection getToClient(String toUserName){

        	for(ClientConnection client: clientsThread){
        		if(client.getUserName().equals(toUserName)){
        			System.out.println("find toUser " + client.userName);
        			return client;
        		}
        	}
        	return null;
		
    }
    
    public String getUserName(){
    	return userName;
    }
    
    public void toClientDisconnected(){
    	toClient = null;
    }
	
    public void disconnect(){
    	isConnected = false;
    }
    
    private ChatMessage composeMessage(MessageState messageState, String name){
        ChatMessage chatMsg = new ChatMessage();

        chatMsg.setMsgContent(name);
        chatMsg.setMessageFlag(messageState.name());
        chatMsg.setOwnMessage(true);

        return chatMsg;
    }
    
    public String setMessageToSend(ChatMessage msg){
        Gson gson = new Gson();
        String jsonMessage = gson.toJson(msg);
        return jsonMessage;
    }

}
