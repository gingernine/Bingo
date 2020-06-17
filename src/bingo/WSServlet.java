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

		DBHandler handler = new DBHandler();
		id = Integer.parseInt(session.getId());

		String[] values = rm.values.split(",");
		handler.insert("board", id, rm.name, values);

		String[]  flags = {"0","0","0","0","0",
				"0","0","0","0","0",
				"0","0","1","0","0",
				"0","0","0","0","0",
				"0","0","0","0","0"};
		handler.insert("flag", id, rm.name, flags);
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

		DBHandler handler = new DBHandler();
		handler.delete("board", id);
		handler.delete("flag", id);
	}

	public static void sendMessage(String message, Session session) {
		session.getAsyncRemote().sendText(message);
	}

	public static ArrayList<Session> getSessionSet() {
		return ses;
	}
}