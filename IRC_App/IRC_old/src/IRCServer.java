import java.net.*;
import java.io.*;
import java.util.Vector;

/*IRCServer code for clients to connect to*/
class IRCServer {
	private final int port = 400;
	private final int maxNum = 100;
	private Vector userList;
	private Vector chatroomList;

	public static void main(String args[]) {
		IRCServer server = new IRCServer();
		server.go();
	}

	public void go() {
		boolean listening = true;
		ServerSocket serverSocket = null;
		userList = new Vector(maxNum);
		chatroomList = new Vector(maxNum);
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("Could not connect to port:" + port + "," + e);
			System.exit(1);
		}
		System.out.println("Connecting on port:" + port);
		while (listening) {
			User user = new User();
			userList.add(user);
			try {
				//Serversocket accepts client connection
				new MultiServerThread(serverSocket.accept(), userList,
						user, chatroomList).start();
			} catch (IOException e) {
			}

		}
		try {
			serverSocket.close();
		} catch (IOException e) {
		}
	}
}

class MultiServerThread extends Thread {
	Socket socket = null;
	Vector userList;
	User user, tempUser;
	Vector chatroomList;
	boolean loginSuccess = true;

	MultiServerThread(Socket socket, Vector userList, User user,
			Vector chatroomList) {
		this.socket = socket;
		this.user = user;
		this.userList = userList;
		this.user.setSocket(this.socket);
		this.chatroomList = chatroomList;
		tempUser = new User();
	}

	public void run() {
		if (socket == null)
			return;

		while (true) {

			try {

				DataInputStream is = new DataInputStream(
						new BufferedInputStream(socket.getInputStream()));
				PrintStream os = new PrintStream(new BufferedOutputStream(
						socket.getOutputStream(), 1024), false);
				String inputLine, outputLine;
				do {
					loginSuccess = true;

					inputLine = is.readLine();
					
					if (!loginSuccess) {
						os.println("loginrefuse");
						os.flush();
						outputLine = "There is a same user's ID.";
						os.println(outputLine);
						os.flush();
					}
				} while (!loginSuccess);
				user.setUserID(inputLine);
				os.println("loginOK");
				os.flush();
				sendServerList();

				while ((inputLine = is.readLine()) != null) {
					String to = "to ";
					String kick = "kick ";
					String remove = "remove";
					String chatwith = "chatwith ";
					int number = inputLine.indexOf((int) ':');
					if (inputLine.length() > 5
							&& inputLine.substring(0, 5).equalsIgnoreCase(
									"creat")
							&& user.getCurrentChatroom() == null) {
						boolean creatSuccess = true;
						IRCChatroom croom = new IRCChatroom();
						for (int i = 0; i < chatroomList.size(); i++) {
							croom = (IRCChatroom) chatroomList.get(i);
							if (croom.getName().equalsIgnoreCase(
									inputLine.substring(6))) {
								creatSuccess = false;
								break;
							}
						}
						if (creatSuccess) {
							IRCChatroom chatroom = new IRCChatroom(
									inputLine.substring(6), user.getUserID());
							chatroomList.add(chatroom);
							user.setIsAdministrator(true);
							user.setCurrentChatroom(inputLine.substring(6));
							os.println("creatOK");
							os.flush();
						} else {
							os.println("creatrefuse");
							os.flush();
							os.println("Chatroom with same name exists.");
							os.flush();
						}

					} else {
						if (inputLine.length() > 4
								&& inputLine.substring(0, 4).equalsIgnoreCase(
										"join")
								&& user.getCurrentChatroom() == null) {
							user.setCurrentChatroom(inputLine.substring(5));
							for (int i = 0; i < userList.size() - 1; i++) {
								tempUser = (User) userList.get(i);
								if (tempUser.getCurrentChatroom() != null
										&& tempUser
												.getCurrentChatroom()
												.equalsIgnoreCase(
														user.getCurrentChatroom())
										&& !tempUser.getUserID()
												.equalsIgnoreCase(
														user.getUserID())) {
									PrintStream clientTo = new PrintStream(
											new BufferedOutputStream(tempUser
													.getSocket()
													.getOutputStream(), 1024),
											false);
									clientTo.println("<" + user.getUserID()
											+ ">I'm here");
									clientTo.flush();

								}
							}
						} else {
							if (inputLine.regionMatches(true, 0, to, 0, 3)) {
								String toName = inputLine.substring(3, number);
								boolean success = false;
								for (int i = 0; i < userList.size() - 1; i++) {
									tempUser = (User) userList.get(i);
									if (tempUser.getCurrent() != null
											&& tempUser.getUserID()
													.equalsIgnoreCase(toName)
											&& tempUser.getCurrent()
													.equalsIgnoreCase(
															user.getUserID())) {
										PrintStream clientTo = new PrintStream(
												new BufferedOutputStream(
														tempUser.getSocket()
																.getOutputStream(),
														1024), false);
										clientTo.println("<from "
												+ user.getUserID()
												+ ">"
												+ inputLine
														.substring(number + 1));
										clientTo.flush();
										clientTo = new PrintStream(
												new BufferedOutputStream(user
														.getSocket()
														.getOutputStream(),
														1024), false);
										clientTo.println("<to "
												+ toName
												+ ">"
												+ inputLine
														.substring(number + 1));
										clientTo.flush();
										success = true;
										break;
									}
								}
								if (!success) {
									PrintStream clientTo = new PrintStream(
											new BufferedOutputStream(user
													.getSocket()
													.getOutputStream(), 1024),
											false);
									clientTo.println("Sorry,no connecting");
									clientTo.flush();
								}
							} else {
								if (inputLine
										.regionMatches(true, 0, kick, 0, 5)) {
									String kickName = inputLine.substring(5);
									if (user.getIsAdministrator())
										for (int i = 0; i < userList.size() - 1; i++) {
											tempUser = (User) userList.get(i);
											if (tempUser.getUserID()
													.equalsIgnoreCase(kickName)) {
												tempUser.setCurrentChatroom(null);
												PrintStream clientTo = new PrintStream(
														new BufferedOutputStream(
																tempUser.getSocket()
																		.getOutputStream(),
																1024), false);
												clientTo.println("kickedfromhere");
												clientTo.flush();
												break;
											}
										}
								}

								else {
									if (inputLine.equalsIgnoreCase(remove)) {
										if (user.getIsAdministrator()) {
											String cr = user
													.getCurrentChatroom();
											for (int i = 0; i < userList.size() - 1; i++) {
												tempUser = (User) userList
														.get(i);
												if (tempUser
														.getCurrentChatroom() != null
														&& tempUser
																.getCurrentChatroom()
																.equalsIgnoreCase(
																		cr)) {
													PrintStream clientTo = new PrintStream(
															new BufferedOutputStream(
																	tempUser.getSocket()
																			.getOutputStream(),
																	1024),
															false);
													clientTo.println("This chatroom has been removed.");
													clientTo.flush();
													clientTo.println("removedchatroom");
													clientTo.flush();
													tempUser.setCurrentChatroom(null);

												}
											}
											for (int i = 0; i < chatroomList
													.size(); i++) {
												IRCChatroom croom = new IRCChatroom();
												croom = (IRCChatroom) chatroomList
														.get(i);
												if (croom.getName()
														.equalsIgnoreCase(cr)) {
													chatroomList
															.removeElement(croom);
													break;
												}
											}
										}
									} else {
										if (inputLine.regionMatches(true, 0,
												chatwith, 0, 9)) {

											String toName = inputLine
													.substring(9);
											for (int i = 0; i < userList.size() - 1; i++) {
												tempUser = (User) userList
														.get(i);
												if (tempUser.getUserID()
														.equalsIgnoreCase(
																toName)) {
													PrintStream clientTo = new PrintStream(
															new BufferedOutputStream(
																	tempUser.getSocket()
																			.getOutputStream(),
																	1024),
															false);
													clientTo.println(user
															.getUserID()
															+ " wants to chat with you.");
													clientTo.flush();
													clientTo.println("chatteris");
													clientTo.flush();
													clientTo.println(user
															.getUserID());
													clientTo.flush();
													os.println("chatteris");
													os.flush();
													os.println(tempUser
															.getUserID());
													os.flush();
													tempUser.setWaiter(user
															.getUserID());
													user.setWaiter(tempUser
															.getUserID());
													break;

												}
											}
										} else {
											//Condition to end chat
											if (inputLine
													.equalsIgnoreCase("endChat")) {
												for (int i = 0; i < userList
														.size() - 1; i++) {
													tempUser = (User) userList
															.get(i);
													if (tempUser.getCurrent() != null
															&& tempUser
																	.getCurrent()
																	.equalsIgnoreCase(
																			user.getUserID())) {
														tempUser.setChatter(null);
														PrintStream clientTo = new PrintStream(
																new BufferedOutputStream(
																		tempUser.getSocket()
																				.getOutputStream(),
																		1024),
																false);
														clientTo.println(user
																.getUserID()
																+ " has ended personal chat.");
														clientTo.flush();
														break;
													}
												}
												user.setChatter(null);
											} else {
												//Condition to accept personal chat request
												if (inputLine
														.equalsIgnoreCase("chatAccept")) {
													user.setChatter(user
															.getNext());
													for (int i = 0; i < userList
															.size() - 1; i++) {
														tempUser = (User) userList
																.get(i);
														if (tempUser
																.getUserID()
																.equalsIgnoreCase(
																		user.getNext())) {
															PrintStream clientTo = new PrintStream(
																	new BufferedOutputStream(
																			tempUser.getSocket()
																					.getOutputStream(),
																			1024),
																	false);
															clientTo.println(user
																	.getUserID()
																	+ " accepted to chat with you.");
															clientTo.flush();
															tempUser.setChatter(user
																	.getUserID());
															break;
														}
													}
												} else {
													//Condition that refuses personal chat request
													if (inputLine
															.equalsIgnoreCase("chatRefuse")) {
														for (int i = 0; i < userList
																.size() - 1; i++) {
															tempUser = (User) userList
																	.get(i);
															if (tempUser
																	.getUserID()
																	.equalsIgnoreCase(
																			user.getNext())) {
																PrintStream clientTo = new PrintStream(
																		new BufferedOutputStream(
																				tempUser.getSocket()
																						.getOutputStream(),
																				1024),
																		false);
																clientTo.println(user
																		.getUserID()
																		+ " refused to chat with you.");
																clientTo.flush();
																break;
															}
														}
													} else {
														//Condition where user quits the chatroom
														if (inputLine
																.equalsIgnoreCase("quit")) {
															userList.removeElement(user);

														} else {
															if (inputLine
																	.equalsIgnoreCase("quitFromHere"))
																user.setCurrentChatroom(null);
															else {
																for (int i = 0; i < userList
																		.size() - 1; i++) {
																	tempUser = (User) userList
																			.get(i);
																	if (tempUser
																			.getCurrentChatroom() != null
																			&& tempUser
																					.getCurrentChatroom()
																					.equalsIgnoreCase(
																							user.getCurrentChatroom())) {
																		PrintStream clientTo = new PrintStream(
																				new BufferedOutputStream(
																						tempUser.getSocket()
																								.getOutputStream(),
																						1024),
																				false);
																		clientTo.println("<"
																				+ user.getUserID()
																				+ ">"
																				+ inputLine);
																		clientTo.flush();

																	}
																}
															}
														}
													}
												}
											}
										}

									}

								}
							}
						}
					}
					sendServerList();
				}

				os.close();
				is.close();
				socket.close();

			} catch (IOException e) {
				// e.printStackTrace();
			}
		}
	}

	public void sendServerList() {

		User tempUser1, tempUser2;
		IRCChatroom tempChatroom;

		try {
			for (int j = 0; j < userList.size() - 1; j++) {
				tempUser1 = (User) userList.get(j);
				PrintStream clientTo = new PrintStream(
						new BufferedOutputStream(tempUser1.getSocket()
								.getOutputStream(), 1024), false);
				clientTo.println("userListStart");
				clientTo.flush();
				if (tempUser1.getCurrentChatroom() == null) {
					for (int i = 0; i < userList.size(); i++) {
						tempUser2 = (User) userList.get(i);
						clientTo.println(tempUser2.getUserID());
						clientTo.flush();
					}
					clientTo.println("userListEnd");
					clientTo.flush();
					for (int i = 0; i < chatroomList.size(); i++) {
						tempChatroom = (IRCChatroom) chatroomList.get(i);
						clientTo.println(tempChatroom.getName());
						clientTo.flush();
					}

				} else {
					for (int i = 0; i < userList.size(); i++) {
						tempUser2 = (User) userList.get(i);
						if (tempUser2.getCurrentChatroom() != null
								&& tempUser2.getCurrentChatroom()
										.equalsIgnoreCase(
												tempUser1.getCurrentChatroom())) {
							clientTo.println(tempUser2.getUserID());
							clientTo.flush();
						}
					}
					clientTo.println("userListEnd");
					clientTo.flush();
				}
				clientTo.println("allListEnd");
				clientTo.flush();
			}

		} catch (IOException e) {
		}

	}
}