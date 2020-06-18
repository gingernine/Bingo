package bingo;

import java.util.ArrayList;
import java.util.Random;

import javax.websocket.Session;

public class Roulette {

	private static ArrayList<Integer> roulette = new ArrayList<>();

	static {
		for (int i = 0; i < 75; i++) {
			roulette.add(i + 1);
		}
	}

	public static ArrayList<Integer> getRouletteList() {
		return roulette;
	}

	public static void sendResOfRoulette() {
		/*
		 * ホストがRouletteを回した時の処理
		 * リストから値をランダム選択し全てのクライアントに送信する
		 */
		ArrayList<Session> ses = WSServlet.getSessionSet();
		int ind = new Random().nextInt(roulette.size());
		Integer num = roulette.get(ind);

		for (Session s : ses) {
			int id = Integer.parseInt(s.getId());
			update(id, num);

			String message = "message:";

			if (isBingo(id)) {
				message += "Bingo!";
			}

			if (isReach(id)) {
				message += "Reach!";
			}

			message += ";num:" + num.toString();
			WSServlet.sendMessage(message, s);
		}
		roulette.remove(num); // 同じ値が再度選ばれることが無いように、取得した値はリストから削除
	}

	private static boolean isBingo(int id) {

		DBHandler handler = new DBHandler();
		int[][] grid = new int[5][5];

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				String SQL = "SELECT * FROM flags WHERE id = " + id;
				grid[i][j] = handler.getResultInt(SQL, "flag" + (i * 5 + j));
			}
		}

		if (grid[0][0] + grid[1][1] + grid[2][2] + grid[3][3] + grid[4][4] == 5) {
			return true;
		}
		if (grid[0][4] + grid[1][3] + grid[2][2] + grid[3][1] + grid[4][0] == 5) {
			return true;
		}
		for (int k = 0; k < 5; k++) {
			if (grid[k][0] + grid[k][1] + grid[k][2] + grid[k][3] + grid[k][4] == 5) {
				return true;
			}
			if (grid[0][k] + grid[1][k] + grid[2][k] + grid[3][k] + grid[4][k] == 5) {
				return true;
			}
		}

		return false;

	}

	private static boolean isReach(int id) {
		DBHandler handler = new DBHandler();
		int[][] grid = new int[5][5];

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				String SQL = "SELECT * FROM flags WHERE id = " + id;
				grid[i][j] = handler.getResultInt(SQL, "flag" + (i * 5 + j));
			}
			System.out.println();
		}

		if (grid[0][0] + grid[1][1] + grid[2][2] + grid[3][3] + grid[4][4] == 4) {
			return true;
		}
		if (grid[0][4] + grid[1][3] + grid[2][2] + grid[3][1] + grid[4][0] == 4) {
			return true;
		}
		for (int k = 0; k < 5; k++) {
			if (grid[k][0] + grid[k][1] + grid[k][2] + grid[k][3] + grid[k][4] == 4) {
				return true;
			}
			if (grid[0][k] + grid[1][k] + grid[2][k] + grid[3][k] + grid[4][k] == 4) {
				return true;
			}
		}

		return false;

	}

	private static void update(int id, Integer num) {

		DBHandler handler = new DBHandler();
		String SQL = "SELECT * FROM board WHERE id = " + id;

		for (int i = 0; i < 25; i++) {
			String res = handler.getResultString(SQL, "value" + i);
			if (res.equals(num.toString())) {
				handler.executeSQL("UPDATE flags SET flag" + i + " = " + 1 + " WHERE id = " + id);
				break;
			}
		}
	}
}