package com.alexsazhko.chatserver;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class MainFrame extends JFrame implements CallBackMessage{
	
	private static final long serialVersionUID = 1L;
	private int port = 6060;
	private JTextArea chatView;
	private JMenuBar menuBar;
	private JMenu file;
	private JMenuItem setPort;
	
	private ServerSocket serverSocket = null;
	private List<ClientConnection> clientsThread = Collections.synchronizedList(new ArrayList<ClientConnection>());
    private ClientConnection clientConnection;
    
    private ExecutorService threadPool;
	

	public MainFrame() {		
		initComponent();
		initConectionThread();
	}

	private void initComponent() {
		setTitle("Chat Server");
	    setSize(550, 450);
	    Container contentPane = getContentPane();
	    contentPane.setLayout(new FlowLayout());
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chatView = new JTextArea(20, 46);
		menuBar = new JMenuBar();
		file = new JMenu("File");
		setPort = new JMenuItem("SetPort");
		setPort.addActionListener(new menuListener());
		menuBar.add(file);
		file.add(setPort);
		setJMenuBar(menuBar);
		contentPane.add(chatView);
		
		setVisible(true);
		
	}

	private void initConectionThread() {
		threadPool = Executors.newCachedThreadPool();
		threadPool.execute(new ServerThread());		
	}
	
	public void setText(String message) {
		chatView.append(message);		
	}

	
	private class menuListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			port = Integer.parseInt(JOptionPane.showInputDialog(MainFrame.this, "Set port"));
			chatView.append("Setted port: " + port);			
		}
		
	}
	
	private class ServerThread implements Runnable {

		public void run() {
			
			try {
				serverSocket = new ServerSocket(port);
		        System.out.println("Server started. Listening to the port" + port + ". Waitng for the client.");				        				       
		    } catch (IOException e) {
		        System.out.println("Could not listen on port: " + port);
		        e.printStackTrace();
		        return;
		    }
			while (true) {				
				try {						
					Socket clientSocket = serverSocket.accept();						
			        System.out.println("Client connected on port" + port);
			        initClientConnection(clientSocket);
			        setWinodwCloseListnerToCloseSocket(clientSocket);
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {						
					e.printStackTrace();
				}
			}
		}
	}

	private void initClientConnection(Socket clientSocket) {

		clientConnection = new ClientConnection(clientSocket, clientsThread);
		clientConnection.registerCallBack(this);
		//clientsThread.add(clientConnection);
		
		threadPool.submit(clientConnection);		
	}
	
	private void setWinodwCloseListnerToCloseSocket(final Socket clientSocket) {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                clientConnection.disconnect();
                threadPool.shutdown();
            }
        });
    }
}
