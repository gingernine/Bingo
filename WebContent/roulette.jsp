<%@page language="java" contentType="text/html" pageEncoding="utf-8"
	import="bingo.Roulette"
	import="bingo.DBHandler"
	import="java.util.ArrayList"%>
<!DOCTYPE html>
<%
	//boolean isPOST = request.getMethod().toLowerCase().equals("post");
	boolean isRoulette = request.getParameter("roulette") != null;
	boolean isCreate = request.getParameter("create") != null;
	boolean isDrop = request.getParameter("drop") != null;
	ArrayList<Integer> roulette = Roulette.getRouletteList();

	if (isRoulette) {
		Roulette.sendResOfRoulette();
	}
	if (isCreate) {
		DBHandler handler = new DBHandler();
		handler.createTable("board");
		handler.createTable("flag");
	}
	if (isDrop) {
		DBHandler handler = new DBHandler();
		handler.dropTable("board");
		handler.dropTable("flag");
	}
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>WebSocketDemo Control</title>
	</head>
	<body>
		<form action="" method="post">
			<input type="submit" name="roulette" value="Roulette">
			<input type="submit" name="create" value="CreateTable">
			<input type="submit" name="drop" value="DropTable">
		</form>
		<%= isRoulette ? roulette.toString() : "" %>
		<%= isCreate ? "table created" : "" %>
		<%= isDrop ? "table droped" : "" %>
	</body>
</html>