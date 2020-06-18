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

		// ビンゴボードの数値をデータベースに保存するためのINSERT文を作成する
		String[] values = rm.values.split(",");
		String SQL1 = "INSERT INTO board VALUES (";
		SQL1 += id;
		SQL1 += " ,'" + rm.name + "'";
		for (int i = 0; i < 25; i++) {
			SQL1 += " ,'" + values[i] + "'";
		}
		SQL1 += ")";
		handler.executeSQL(SQL1);

		// ビンゴ判定用のフラグ列をデータベースに保存するためのINSERT文を作成する
		int[] flags = {0, 0, 0, 0, 0,
				0, 0, 0, 0, 0,
				0, 0, 1, 0, 0,
				0, 0, 0, 0, 0,
				0, 0, 0, 0, 0};
		String SQL2 = "INSERT INTO flags VALUES (";
		SQL2 += id;
		SQL2 += " ,'" + rm.name + "'";
		for (int i = 0; i < 25; i++) {
			SQL2 += " ," + flags[i];
		}
		SQL2 += ")";
		handler.executeSQL(SQL2);
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
		handler.executeSQL("DELETE FROM board WHERE id = " + id);
		handler.executeSQL("DELETE FROM flags WHERE id = " + id);
	}

	public static void sendMessage(String message, Session session) {
		session.getAsyncRemote().sendText(message);
	}

	public static ArrayList<Session> getSessionSet() {
		return ses;
	}
}