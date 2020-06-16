package board;

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

	public static void sendResOfRoulette() {
		/*
		 * Rouletteボタンが押された時の処理
		 * ランダムな要素を取得する
		 * 各クライアントブラウザーに取得した数値を送信する
		 */
		ArrayList<Session> ses = WSServlet.getSessionSet();
		int ind = new Random().nextInt(roulette.size());
		Integer num = roulette.get(ind);

		for (Session s : ses) {
			WSServlet.sendMessage(num.toString(), s);
		}
		roulette.remove(num); // 同じ数値を再度取得することが無いように、取得した数値は削除する
	}

	public static ArrayList<Integer> getRouletteList() {
		return roulette;
	}
}