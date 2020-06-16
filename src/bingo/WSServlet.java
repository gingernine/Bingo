package bingo;

import java.util.ArrayList;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/wsservlet", decoders = MyDecoder.class)
public class WSServlet {

	private static ArrayList<Session> ses = new ArrayList<>();
	private Session session;
	private int id; // session id

	@OnMessage
	public void onMessage(ReceivedMessage rm) {
		System.out.println("Message received from " + session.getId());
		sendMessage("echo => " + rm.name + ":" + rm.values, session);

		id = Integer.parseInt(session.getId());
		new DBHandler("test").insert("test3", id, rm.name, rm.values);
	}

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("onOpen : " + session);
		this.session = session;
		ses.add(session);
	}

	@OnClose
	public void onClose(Session session) {
		System.out.println("onClose : " + session);
		ses.remove(session);

		new DBHandler("test").delete("test3", id);
	}

	public static void sendMessage(String message, Session session) {
		session.getAsyncRemote().sendText(message);
	}

	public static ArrayList<Session> getSessionSet() {
		return ses;
	}
}