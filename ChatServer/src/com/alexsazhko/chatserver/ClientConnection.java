package com.alexsazhko.chatserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import org.json.*;

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
	private Contact contact;

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
	    			
	    			MessageState state = getMessageFlag(stringMessage);
	    			
	    			switch(state){
	    			case SEARCH:  
	    				ContactList contactList = new ContactList();
	    				ArrayList<Contact> contacts = new  ArrayList<Contact>();	    				
	    				chatMessage = getChatMessage(stringMessage);
	    				
    					synchronized (clientsThread) {

    						for(ClientConnection findedContact: clientsThread){
    							if(findedContact.contact.getName().equalsIgnoreCase(chatMessage.getMsgContent())){   								   								
    								contacts.add(findedContact.contact);
    							}
    						}
    						contactList.setClients(contacts);
    						dataOutputStream.writeUTF(setContactListToSend(contactList));
	    					dataOutputStream.flush();
	    				
	    				}
	  
	    				break;
	    				case NEW:  
	    					contact = getContact(stringMessage);
		    				userName = contact.getName();
	    					synchronized (clientsThread) {
		    					clientsThread.add(this);
		    				}
	    					
		    				System.out.println(clientsThread.size());			
		    				callBackMessage.setText("User " + contact.getName() + " joined to the chat" + "\n");
		    				break;
	    				case MESSAGE:
	    					chatMessage = getChatMessage(stringMessage);
	    					toUserName = chatMessage.getToUserName();
	    					callBackMessage.setText(userName + " to " + toUserName + chatMessage.getMsgContent() + "\n");
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
		    				break;
	    				case END:
	    					chatMessage = getChatMessage(stringMessage);
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
    
    public String setMessageToSend(ChatMessage msg){
        Gson gson = new Gson();
        String jsonMessage = gson.toJson(msg);
        return jsonMessage;
    }
    
    private Contact getContact(String message){
        Gson gson = new Gson();
        ContactNew contactNew = gson.fromJson(message, ContactNew.class);
        Contact contact = contactNew.getContact();;
    	return contact;
    }
    
    private MessageState getMessageFlag(String json){
    	MessageState messageFlag = MessageState.MESSAGE;
    	
    	try {
            JSONObject jsonObject = new JSONObject(json);                     
            messageFlag = MessageState.valueOf(jsonObject.getString("messageFlag"));      
        } catch (JSONException e) {
            e.printStackTrace();
        }
    	return messageFlag;
    }
    
    public String setContactListToSend(ContactList contactList){
        Gson gson = new Gson();
        String jsonMessage = gson.toJson(contactList);
        return jsonMessage;
    }
    

}
