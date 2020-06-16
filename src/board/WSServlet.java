package board;

import java.util.ArrayList;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/board")
public class WSServlet {
	private static ArrayList<Session> ses = new ArrayList<>();

	@OnMessage
	public void onMessage(String message, Session session) {
		System.out.println("Message received from " + session.getId());
		sendMessage("echo => " + message, session);

		int id = Integer.parseInt(session.getId());
		new DBHandler("test").insert("test2", id, message);
	}

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("onOpen : " + session);
		ses.add(session);
	}

	@OnClose
	public void onClose(Session session) {
		System.out.println("onClose : " + session);
		ses.remove(session);

		int id = Integer.parseInt(session.getId());
		new DBHandler("test").delete("test2", id);
	}

	public static void sendMessage(String message, Session session) {
		session.getAsyncRemote().sendText(message);
	}

	public static ArrayList<Session> getSessionSet() {
		return ses;
	}
}