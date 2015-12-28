package com.rwoar.moo.client.applet;

import java.io.IOException;

import javax.swing.JApplet;
import javax.swing.SwingUtilities;

import com.rwoar.moo.client.connection.ConnectionPanel;


public class Applet extends JApplet {

	private static final long serialVersionUID = -5091721011048350669L;
	private String host = null;
	private Integer port = null;
	private ConnectionPanel cp = null;
	
	public void init() {
		//Execute a job on the event-dispatching thread; creating this applet's GUI.
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					createGUI();
				}
			});
		} catch (Exception e) { 
			System.err.println("createGUI didn't complete successfully");
		}
	}
	
	public void stop(){
		if (cp.getConnection() != null)
			try {
				cp.getConnection().closeConnection();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	private void createGUI() {
		setParameters();
		
		//Create and set up the content pane.
		cp = new ConnectionPanel(this.host, this.port, getParameter("input_bgcolor"), 
				getParameter("input_fgcolor"), getParameter("output_bgcolor"), 
				getParameter("output_fgcolor"),	getParameter("echo_color"), 
				getParameter("echo_prefix"), getParameter("font"), 
				getParameter("recall_amount"));
		cp.setOpaque(true); 
		setContentPane(cp);        
	}

	private void setParameters(){
		this.host = getParameter("host");
		this.port = Integer.parseInt(getParameter("port"));
		
		// Required fields
		if (host == null){
			System.err.println("You must specify the host parameter.");
			System.exit(1);
		} else if(port == null){
			System.err.println("You must specify the port parameter.");
			System.exit(1);
		}
		
	}

}
