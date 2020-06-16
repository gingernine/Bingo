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

	public static void sendResOfRoulette() {

		ArrayList<Session> ses = WSServlet.getSessionSet();
		int ind = new Random().nextInt(roulette.size());
		Integer num = roulette.get(ind);

		for (Session s : ses) {
			WSServlet.sendMessage(num.toString(), s);
		}
		roulette.remove(num);
	}

	public static ArrayList<Integer> getRouletteList() {
		return roulette;
	}
}