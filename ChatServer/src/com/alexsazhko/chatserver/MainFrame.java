package com.alexsazhko.chatserver;

import java.awt.Container;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;

public class MainFrame extends JFrame implements CallBackMessage{
	
	private static final long serialVersionUID = 1L;
	private int port;
	private JTextArea chatView;
	private JTextArea chatWriteField;
	private JMenuBar menuBar;
	private JMenu file;
	private JMenuItem setPort;
	private JButton buttonSend;
	private Dialog setPortDialog;

	public void MainFrame() {		
		initComponent();
	}

	private void initComponent() {
		setTitle("Chat Server");
	    setSize(550, 450);
	    Container contentPane = getContentPane();
	    contentPane.setLayout(new FlowLayout());
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chatView = new JTextArea(20, 46);
		chatWriteField = new JTextArea(5, 40);
		buttonSend = new JButton("Send");
		menuBar = new JMenuBar();
		file = new JMenu("File");
		setPort = new JMenuItem("SetPort");
		setPort.addActionListener(new menuListener());
		menuBar.add(file);
		file.add(setPort);
		setJMenuBar(menuBar);
		contentPane.add(chatView);
		contentPane.add(chatWriteField);
		contentPane.add(buttonSend);
			
		setVisible(true);
		
	}
	
	public void setText(String message) {
		chatView.append(message);		
	}

	
	private class menuListener implements ActionListener{

		public void actionPerformed(ActionEvent arg0) {
			
		}
		
	}

}
