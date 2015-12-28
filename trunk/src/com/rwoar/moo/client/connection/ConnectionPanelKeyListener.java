package com.rwoar.moo.client.connection;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class ConnectionPanelKeyListener implements KeyListener {
	
	private ConnectionPanel connPanel;
	
	public ConnectionPanelKeyListener(ConnectionPanel connPanel){
		this.connPanel = connPanel;
	}
	
	public void keyPressed(KeyEvent arg0) {
		try{
			// If the key is the UP button
			if (arg0.getKeyCode() == 38
					&& arg0.getModifiers() == 0){
				connPanel.getUserInput().setText(connPanel.getHistory().next());
			}
			// If the key is the DOWN button
			if (arg0.getKeyCode() == 40
					&& arg0.getModifiers() == 0){
				connPanel.getUserInput().setText(connPanel.getHistory().previous());
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
}
