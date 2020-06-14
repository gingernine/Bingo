<%@page language="java" contentType="text/html" pageEncoding="utf-8"
	import="board1.Board"
	import="board1.Roulette"
	import="java.util.ArrayList"
	import="java.util.Random"
	import="javax.websocket.Session"%>
<!DOCTYPE html>
<%
	boolean isPOST = request.getMethod().toLowerCase().equals("post");
	ArrayList<Integer> roulette = Roulette.getRouletteList();

	if (isPOST) {
		/*
		 * Rouletteボタンが押された時の処理
		 * Boardクラスからrouletteリストを受け取り、そこからランダムな要素を取得する
		 * 各クライアントブラウザーに取得した数値を送信する
		 */
		ArrayList<Session> ses = Board.getSessionSet();
		int ind = new Random().nextInt(roulette.size());
		Integer num = roulette.get(ind);

		for (Session s : ses) {
			Board.sendMessage(num.toString(), s);
		}
		roulette.remove(num); // 同じ数値を再度取得することが無いように、取得した数値は削除する
	}
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>WebSocketDemo Control</title>
	</head>
	<body>
		<%= isPOST ? roulette.toString() : "" %>
		<form action="" method="post">
			<input type="submit" name="submit" value="Roulette">
		</form>
	</body>
</html>