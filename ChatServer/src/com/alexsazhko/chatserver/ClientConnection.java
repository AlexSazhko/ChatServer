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

	public ClientConnection(Socket clientSocket,
			List<ClientConnection> clientsThread) {
		this.clientSocket = clientSocket;
		this.clientsThread = clientsThread;
		isConnected = true;
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
	    			System.out.println("own" + chatMessage.isOwnMessage());
	    			if (chatMessage != null && chatMessage.getMessageFlag().equals("END")){
	    				clientsThread.remove(this);
	    				break;
	    			}

		            callBackMessage.setText(chatMessage.getUserName() + ": " + chatMessage.getMsgContent() + "\n");
	            	for(ClientConnection client: clientsThread){
	            		if(client != this){
	            			client.dataOutputStream.writeUTF(stringMessage);
	            			client.dataOutputStream.flush();
	            		}
                    }
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
            } //End of fi
		
	}
    
	
	public void registerCallBack(CallBackMessage callBackMessage){
		this.callBackMessage = callBackMessage;
	}
	
    private ChatMessage getChatMessage(String json){
        Gson gson = new Gson();
        ChatMessage message = gson.fromJson(json, ChatMessage.class);
        return message;
    }
	
    public void disconnect(){
    	isConnected = false;
    }

}
