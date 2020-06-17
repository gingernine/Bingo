package bingo;

public class Test {
	public static void main(String[] args) {
		DBHandler handler = new DBHandler();
		handler.createTable("board");
	}
}
