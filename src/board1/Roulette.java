package board1;

import java.util.ArrayList;

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
}