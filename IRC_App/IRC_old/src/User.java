import java.net.*;

/* User class to set the details of the user*/

class User {
	private String userID;
	private String currentChatroom = null;
	private Socket socket;
	private boolean isAdministrator = false;
	private String current;
	private String next;

	public User() {

	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getCurrentChatroom() {
		return currentChatroom;
	}

	public void setCurrentChatroom(String chatroom) {
		currentChatroom = chatroom;
	}

	public boolean getIsAdministrator() {
		return isAdministrator;
	}

	public void setIsAdministrator(boolean isAdministrator) {
		this.isAdministrator = isAdministrator;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public String getCurrent() {
		return current;
	}

	public void setChatter(String current) {
		this.current = current;
	}

	public String getNext() {
		return next;
	}

	public void setWaiter(String next) {
		this.next = next;
	}
}