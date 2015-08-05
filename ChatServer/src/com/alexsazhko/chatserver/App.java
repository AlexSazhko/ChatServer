package com.alexsazhko.chatserver;

import javax.swing.SwingUtilities;

public class App {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainFrame mainFrame = new MainFrame();
			}
		});	

	}

}
