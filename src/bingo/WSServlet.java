package bingo;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.fasterxml.jackson.databind.ObjectMapper;

@ServerEndpoint(value = "/wsservlet")
public class WSServlet {

	private static List<Session> ses = new CopyOnWriteArrayList<>(); // セッションを格納
	private static Map<Session, int[][]> flags = new Hashtable<>(); // セッションとビンゴ判定用の配列を格納
	private Session session;
	private int id; // session id

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("onOpen : " + session);
		this.session = session;
		ses.add(session);
	}

	@OnMessage
	public void onMessage(String message) {
		System.out.println("Message received from " + session.getId());

		DBHandler handler = new DBHandler();
		id = Integer.parseInt(session.getId());

		// ビンゴボードの数値をデータベースに保存するためのINSERT文を作成する
		ObjectMapper mapper = new ObjectMapper();
		ReceivedMessage rm = null;
		try {
			rm = mapper.readValue(message, ReceivedMessage.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String SQL = "INSERT INTO board VALUES (";
		SQL += id;
		SQL += " ,'" + rm.username + "'";
		for (int i = 0; i < 25; i++) {
			SQL += " ,'" + rm.values[i] + "'";
		}
		SQL += ")";
		handler.executeSQL(SQL);

		// ビンゴ判定用のフラグ
		int[][] grid = new int[5][5];
		grid[2][2] = 1;
		flags.put(session, grid);

	}

	@OnClose
	public void onClose(Session session) {
		System.out.println("onClose : " + session);
		ses.remove(session);

		DBHandler handler = new DBHandler();
		handler.executeSQL("DELETE FROM board WHERE id = " + id);
	}

	public static void sendMessage(String message, Session session) {
		session.getAsyncRemote().sendText(message);
	}

	public static List<Session> getSessionSet() {
		return ses;
	}

	public static Map<Session, int[][]> getFlags() {
		return flags;
	}
}