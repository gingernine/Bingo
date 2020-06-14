package board1;

import java.util.ArrayList;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/board1")
public class Board {
	private static ArrayList<Session> ses = new ArrayList<>();
	private static ArrayList<Integer> roulette = new ArrayList<>();

	static {
		for (int i = 0; i < 75; i++) {
			roulette.add(i + 1);
		}
	}

	@OnMessage
	public void onMessage(String message) {
		System.out.println("Message received : " + message);
		for (Session s : ses) {
			sendMessage(message, s);
		}
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
	}

	public static void sendMessage(String message, Session session) {
		session.getAsyncRemote().sendText(message);
	}

	public static ArrayList<Session> getSessionSet() {
		return ses;
	}

	public static ArrayList<Integer> getRouletteList() {
		return roulette;
	}
}