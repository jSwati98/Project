/*Chatroom class sets chatroom properties*/

class IRCChatroom {
	private String name;
	private String administratorID;
	private String[] users;
	private int currentUserNumber = 0;

	public IRCChatroom() {
	}

	public IRCChatroom(String name, String administratorID) {
		this.name = name;
		this.administratorID = administratorID;
	}

	public String getName() {
		return name;
	}

	public String getAdministratorID() {
		return administratorID;
	}

	public String[] getUsers() {
		return users;
	}

}