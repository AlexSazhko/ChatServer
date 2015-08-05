package com.alexsazhko.chatserver;

import java.net.Socket;
import java.util.List;

public class ClientConnection implements Runnable{
	
	private CallBackMessage callBackMessage;
	private boolean isConnected;

	public ClientConnection(Socket clientSocket,
			List<ClientConnection> clientsThread) {

	}

	public void run() {

		
	}
	
	public void registerCallBack(CallBackMessage callBackMessage){
		this.callBackMessage = callBackMessage;
	}
	
    public void disconnect(){
    	isConnected = false;
    }

}
