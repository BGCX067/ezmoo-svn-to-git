package com.rwoar.moo.client.connection;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import com.rwoar.moo.client.utils.ClientDefaults;
import com.rwoar.moo.client.utils.HistoryLinkedList;
import com.rwoar.moo.client.utils.Parser;



/**
 * This code was edited or generated using CloudGarden's Jigloo
 * SWT/Swing GUI Builder, which is free for non-commercial
 * use. If Jigloo is being used commercially (ie, by a corporation,
 * company or business for any purpose whatever) then you
 * should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details.
 * Use of Jigloo implies acceptance of these licensing terms.
 * A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
 * THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
 * LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class ConnectionPanel extends javax.swing.JPanel implements Runnable {
	private static final long serialVersionUID = -8091956261775488267L;

	private JTextArea user_input;
	private ColorPane server_output;
	private JScrollPane jScrollPane1;
	private Connection conn;
	private HistoryLinkedList<String> history = null;
	private String host;
	private int port;

	private Color input_bgcolor;
	private Color input_fgcolor;
	private Color output_bgcolor;
	private Color output_fgcolor;
	private Color echo_color;
	private String echo_prefix;
	private Font font;

	/**
	 * Auto-generated main method to display this 
	 * JPanel inside a new JFrame.
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		//ConnectionPanel cp = new ConnectionPanel("67.43.244.40", 5555);
		ConnectionPanel cp = new ConnectionPanel("lisdude.com", 5555, null, null, 
				null, null, null, null, null, null);
		frame.setTitle("MOO Client");
		frame.getContentPane().add(cp);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.setSize(800,600);
		frame.setVisible(true);
	}

	/**
	 * 
	 * @param host
	 * @param port
	 * @param input_bgcolor Hex code for input background color
	 * @param input_fgcolor Hex code for input foreground color
	 * @param output_bgcolor Hex code for output background color
	 * @param output_fgcolor Hex code for output foreground color
	 * @param echo_color Hex code for local echo color
	 * @param echo_prefix Echo prefix
	 * @param font Font string
	 */
	public ConnectionPanel(String host, int port, String input_bgcolor, String input_fgcolor,
			String output_bgcolor, String output_fgcolor, String echo_color, 
			String echo_prefix, String font, String recall_amount) {
		super();

		this.host = host;
		this.port = port;
		this.input_bgcolor = Parser.parseColor(input_bgcolor, ClientDefaults.INPUT_BGCOLOR);
		this.input_fgcolor = Parser.parseColor(input_fgcolor, ClientDefaults.INPUT_FGCOLOR);
		this.output_bgcolor = Parser.parseColor(output_bgcolor, ClientDefaults.OUTPUT_BGCOLOR);
		this.output_fgcolor = Parser.parseColor(output_fgcolor, ClientDefaults.OUTPUT_FGCOLOR);
		this.echo_color = Parser.parseColor(echo_color, ClientDefaults.ECHO_COLOR);
		this.echo_prefix = echo_prefix != null ? echo_prefix : ClientDefaults.ECHO_PREFIX;
		this.font = Parser.parseFont(font, ClientDefaults.FONT);
		this.history = new HistoryLinkedList<String>(recall_amount != null 
				? Integer.parseInt(recall_amount) : ClientDefaults.RECALL_AMOUNT);

		initGUI();

		Thread t = new Thread(this, "openConnAndReadLineFromConnection");
		t.start();

	}

	private void initGUI() {
		try {
			BorderLayout thisLayout = new BorderLayout();
			this.setLayout(thisLayout);
			setPreferredSize(new Dimension(400, 300));
			{
				user_input = new JTextArea();
				user_input.addAncestorListener(new RequestFocusListener());
				user_input.setRows(2);
				
				InputMap inputMap = user_input.getInputMap();
				ActionMap actionMap = user_input.getActionMap();
				Object sendText = "SEND_TEXT";
				inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), sendText);
				actionMap.put(sendText, new AbstractAction() {
					private static final long serialVersionUID = 1389604717029590749L;
					public void actionPerformed(ActionEvent arg0) {
						String text = getUserInput().getText();
						getServerOutput().appendColor(getEchoColor(), getEchoPrefix()+text+"\n");
						getUserInput().setText(null);
						getConnection().writeLine(text);
						getHistory().addFirst(text);
					}
				});
				
				this.add(getUserInput(), BorderLayout.SOUTH);
				user_input.setFont(this.font);
				user_input.setBackground(this.input_bgcolor);
				user_input.setForeground(this.input_fgcolor);
				user_input.addKeyListener(new ConnectionPanelKeyListener(this));
				user_input.setLineWrap(true);
				user_input.setWrapStyleWord(true);


			}
			{
				jScrollPane1 = new JScrollPane();
				this.add(jScrollPane1, BorderLayout.CENTER);
				jScrollPane1.setPreferredSize(new java.awt.Dimension(400, 277));
				{
					server_output = new ColorPane(this.output_fgcolor);
					jScrollPane1.setViewportView(getServerOutput());
					server_output.setBackground(this.output_bgcolor);
					server_output.setForeground(this.output_fgcolor);
					server_output.setFont(this.font);
					server_output.setEditable(false);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	JTextArea getUserInput() {
		return user_input;
	}

	ColorPane getServerOutput() {
		return server_output;
	}

	HistoryLinkedList<String> getHistory(){
		return this.history;
	}

	Color getEchoColor(){
		return this.echo_color;
	}

	String getEchoPrefix(){
		return this.echo_prefix;
	}

	public Connection getConnection(){
		return this.conn;
	}

	@Override
	public void run() {
		String line;

		try {
			conn = new Connection(host, port);
			while (true){
				//Thread.sleep(10);
				if ((line = conn.readLine()) != null)
					server_output.appendANSI(line + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
