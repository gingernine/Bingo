package bingo;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/wsservlet", decoders = MyDecoder.class)
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
	public void onMessage(ReceivedMessage rm) {
		System.out.println("Message received from " + session.getId());

		DBHandler handler = new DBHandler();
		id = Integer.parseInt(session.getId());

		// ビンゴボードの数値をデータベースに保存するためのINSERT文を作成する
		String[] values = rm.values.split(",");
		String SQL = "INSERT INTO board VALUES (";
		SQL += id;
		SQL += " ,'" + rm.name + "'";
		for (int i = 0; i < 25; i++) {
			SQL += " ,'" + values[i] + "'";
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
		handler.executeSQL("DELETE FROM flags WHERE id = " + id);
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