import java.io.*;
import java.net.*;
import java.util.Vector;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/*IRC client code. It uses multithreading Client connect to server on port no specified*/
public class IRCClient {
	public static final int port = 400;
	public static final int maxNumber = 100;

	public static void main(String[] args) {
		IRCClient client = new IRCClient();
		client.go();
	}

	public void go() {

		try {
			Vector userList = new Vector(maxNumber);
			Vector chatroomList = new Vector(maxNumber);
			Socket dgSocket = new Socket("localhost", port);

			PrintStream os = new PrintStream(dgSocket.getOutputStream());
			DataInputStream is = new DataInputStream(dgSocket.getInputStream());
			String name;
			String fromServer;
			String message;

			new MultiClientThread(dgSocket, userList, chatroomList).start();

		} catch (UnknownHostException e) {
			System.err.println(e);
		} catch (Exception e) {
			System.err.println("Exception:" + e);
		}
	}
}

class MultiClientThread extends Thread implements WindowListener,
		ActionListener {
	/*Making UI for Client*/
	JFrame loginFrame = new JFrame();
	JLabel ipLabel = new JLabel();
	JLabel portLabel = new JLabel();
	JLabel idLabel = new JLabel();
	JTextField portTextField = new JTextField();
	JTextField ipTextField = new JTextField();
	JTextField idTextField = new JTextField();
	JButton loginButton = new JButton();
	JButton quitButton = new JButton();

	JFrame chatroomListFrame = new JFrame();
	JLabel chatroomListLabel = new JLabel();
	List chatroomNameList = new List();
	JButton creatChatroomButton = new JButton();
	JButton joinChatroomButton = new JButton();
	JButton quitAllButton = new JButton();
	JPanel buttonPanel = new JPanel();
	JTextField creatChatroomTextField = new JTextField();

	JFrame chatroomFrame = new JFrame();
	JTextArea chatTextArea = new JTextArea();
	List userNameList = new List();
	JTextField chatTextField = new JTextField();
	JButton sendButton = new JButton();
	JButton sendToButton = new JButton();
	JButton kickButton = new JButton();
	JButton chatButton = new JButton();
	JButton quitHereButton = new JButton();
	JButton removeButton = new JButton();
	JButton acceptChatButton = new JButton();
	JButton refuseChatButton = new JButton();
	JButton endChatButton = new JButton();
	JPanel westPanel = new JPanel();
	JPanel eastPanel = new JPanel();

	Socket socket = null;
	String fromServer;
	Vector userList;
	Vector chatroomList;
	DataInputStream is;
	PrintStream os;
	String ownName;
	String name;

	public MultiClientThread(Socket socket, Vector userList, Vector chatroomList) {
		this.socket = socket;
		this.userList = userList;
		this.chatroomList = chatroomList;
		try {
			os = new PrintStream(socket.getOutputStream());
			is = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
		}
	}

	public void run() {
		//setting UI components
		loginFrame.getContentPane().setBackground(
		UIManager.getColor("Table.shadow"));
		loginFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		loginFrame.setLocale(java.util.Locale.getDefault());
		loginFrame.setResizable(true);
		loginFrame.addWindowListener(this);
		loginFrame.getContentPane().setLayout(null);
		idLabel.setText("Username :");
		idLabel.setBounds(new Rectangle(45, 51, 68, 23));
		idTextField.setText("");
		idTextField.setBounds(new Rectangle(130, 51, 84, 22));
		quitButton.setBounds(new Rectangle(160, 125, 71, 27));
		quitButton.setActionCommand("Quit");
		quitButton.setText("Quit");
		quitButton.addActionListener(this);
		loginButton.setBounds(new Rectangle(70, 125, 75, 27));
		loginButton.addActionListener(this);
		loginButton.setActionCommand("Join");
		loginButton.setText("Login");
		loginFrame.getContentPane().add(ipLabel, null);
		loginFrame.getContentPane().add(idLabel, null);
		loginFrame.getContentPane().add(quitButton, null);
		loginFrame.getContentPane().add(loginButton, null);
		loginFrame.getContentPane().add(idTextField, null);
		loginFrame.setSize(300, 300);

		loginFrame.setVisible(true);

		chatroomListFrame.addWindowListener(this);
		chatroomListLabel.setText("Chatrooms");
		creatChatroomButton.setText("Create");
		creatChatroomButton.addActionListener(this);
		joinChatroomButton.setText("Join");
		joinChatroomButton.addActionListener(this);
		quitAllButton.setText("Quit");
		quitAllButton.addActionListener(this);
		chatroomListFrame.getContentPane().add(BorderLayout.NORTH,
				chatroomListLabel);
		chatroomListFrame.getContentPane().add(BorderLayout.CENTER,
				chatroomNameList);
		creatChatroomTextField.setColumns(30);
		buttonPanel.add(creatChatroomTextField);
		buttonPanel.add(creatChatroomButton);
		buttonPanel.add(joinChatroomButton);
		buttonPanel.add(quitAllButton);
		chatroomListFrame.getContentPane().add(BorderLayout.SOUTH, buttonPanel);
		chatroomListFrame.setSize(600, 400);

		chatroomFrame.addWindowListener(this);
		chatTextArea.setEditable(false);
		westPanel.setLayout(new BorderLayout());
		westPanel.add(BorderLayout.CENTER, chatTextArea);
		JPanel sendPanel = new JPanel();
		chatTextField.setColumns(20);
		sendPanel.setLayout(new GridLayout(1, 3));
		sendButton.setText("Send");
		sendButton.addActionListener(this);
		sendToButton.setText("Send Personal Message");
		sendToButton.addActionListener(this);
		sendPanel.add(chatTextField);
		sendPanel.add(sendButton);
		sendPanel.add(sendToButton);
		westPanel.add(BorderLayout.SOUTH, sendPanel);
		eastPanel.setLayout(new GridLayout(6, 1));
		chatButton.setText("Invite for Chat");
		chatButton.addActionListener(this);
		acceptChatButton.setText("Accept");
		acceptChatButton.addActionListener(this);
		refuseChatButton.setText("Refuse");
		refuseChatButton.addActionListener(this);
		endChatButton.setText("End Personal Chat");
		endChatButton.addActionListener(this);
		quitHereButton.setText("Quit");
		quitHereButton.addActionListener(this);
		eastPanel.add(userNameList);
		eastPanel.add(chatButton);
		eastPanel.add(acceptChatButton);
		eastPanel.add(refuseChatButton);
		eastPanel.add(endChatButton);
		eastPanel.add(quitHereButton);
		chatroomFrame.getContentPane().add(BorderLayout.CENTER, westPanel);
		chatroomFrame.getContentPane().add(BorderLayout.EAST, eastPanel);
		chatroomFrame.setSize(800, 700);

		try {
			DataInputStream is = new DataInputStream(socket.getInputStream());
			while (true) {
				fromServer = is.readLine();
				if (fromServer.length() != 0) {
					if (fromServer.equalsIgnoreCase("loginOK")) {
						loginFrame.hide();
						chatroomListFrame.setVisible(true);
					} else {
						if (fromServer.equalsIgnoreCase("loginRefuse"))
							idTextField.setText(is.readLine());
						else {
							if (fromServer.equalsIgnoreCase("creatOK")) {
								chatroomListFrame.hide();
								chatTextArea.setText(null);
								chatroomFrame.setVisible(true);
							} else {
								if (fromServer.equalsIgnoreCase("creatrefuse"))
									creatChatroomTextField.setText(is
											.readLine());
								else {
									if (fromServer
											.equalsIgnoreCase("kickedfromhere")
											|| fromServer
													.equalsIgnoreCase("removedchatroom")) {
										chatroomFrame.hide();
										chatroomListFrame.setVisible(true);
									} else {
										if (fromServer
												.equalsIgnoreCase("chatteris"))
											name = is.readLine();
										else {
											if (fromServer
													.equalsIgnoreCase("userListStart")) {
												fromServer = is.readLine();
												while (!fromServer
														.equalsIgnoreCase("userListEnd")) {
													userList.add(new String(
															fromServer));
													fromServer = is.readLine();
												}
												fromServer = is.readLine();
												while (!fromServer
														.equalsIgnoreCase("allListEnd")) {
													chatroomList
															.add(new String(
																	fromServer));
													fromServer = is.readLine();
												}
												userNameList.removeAll();
												for (int i = 0; i < userList
														.size(); i++) {
													String non = (String) userList
															.get(i);
													if (!non.equalsIgnoreCase("null"))
														;
													userNameList
															.add((String) userList
																	.get(i));
												}
												chatroomNameList.removeAll();
												for (int i = 0; i < chatroomList
														.size(); i++)
													chatroomNameList
															.add((String) chatroomList
																	.get(i));
												userList.removeAllElements();
												chatroomList
														.removeAllElements();
											} else
												chatTextArea.append(fromServer
														+ "\n");
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (IOException e) {
		}
	}

	public void windowClosing(WindowEvent e) {
		if (e.getSource() == loginFrame)
			System.exit(1);
		if (e.getSource() == chatroomListFrame
				|| e.getSource() == chatroomFrame) {
			os.println("quit");
			os.flush();
			System.exit(1);
		}
	}

	public void windowOpened(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}

	public void actionPerformed(ActionEvent e) {
		//List of all the functions a client can perform is written to output stream, 
		//which will be processed at the server.
		if (e.getSource() == loginButton) {
			ownName = idTextField.getText();
			chatroomFrame.setTitle(ownName);
			os.println(ownName);
			os.flush();

		}
		if (e.getSource() == quitButton) {
			System.exit(1);
		}
		if (e.getSource() == creatChatroomButton) {
			os.println("creat " + creatChatroomTextField.getText());
			os.flush();
		}
		if (e.getSource() == joinChatroomButton) {
			os.println("join " + chatroomNameList.getSelectedItem());
			os.flush();
			chatroomListFrame.hide();
			chatTextArea.setText(null);
			chatTextArea.setText("<" + ownName + ">I'm here!\n");
			chatroomFrame.setVisible(true);
		}
		if (e.getSource() == quitAllButton) {
			os.println("quit");
			os.flush();
			System.exit(1);
		}
		if (e.getSource() == sendButton) {
			os.println(chatTextField.getText());
			os.flush();
			chatTextField.setText(null);
		}
		if (e.getSource() == chatButton) {
			System.out.println("userNameList--->"
					+ userNameList.getSelectedItem());
			os.println("chatwith " + userNameList.getSelectedItem());
			os.flush();
		}
		if (e.getSource() == kickButton) {
			os.println("kick " + userNameList.getSelectedItem());
			os.flush();
		}
		if (e.getSource() == quitHereButton) {
			os.println("quitFromHere");
			os.flush();
			chatroomFrame.hide();
			chatroomListFrame.setVisible(true);
		}
		if (e.getSource() == sendToButton) {
			os.println("to " + name + ":" + chatTextField.getText());
			os.flush();
			chatTextField.setText(null);
		}
		if (e.getSource() == removeButton) {
			os.println("remove");
			os.flush();
		}
		if (e.getSource() == acceptChatButton) {
			os.println("chatAccept");
			os.flush();
		}
		if (e.getSource() == refuseChatButton) {
			os.println("chatRefuse");
			os.flush();
		}
		if (e.getSource() == endChatButton) {
			os.println("endChat");
			os.flush();
		}
	}
}
