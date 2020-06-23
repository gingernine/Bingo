<%@page language="java" contentType="text/html" pageEncoding="utf-8"
	import="bingo.Roulette"
	import="bingo.DBHandler"
	import="java.util.ArrayList"
	import="java.util.List"%>

<%
	//boolean isPOST = request.getMethod().toLowerCase().equals("post");
	boolean isRoulette = request.getParameter("roulette") != null;
	boolean isCreate = request.getParameter("create") != null;
	boolean isDrop = request.getParameter("drop") != null;
	List<Integer> roulette = Roulette.getRouletteList();

	if (isRoulette) {
		Roulette.sendResOfRoulette();
	}

	if (isCreate) {

		DBHandler handler = new DBHandler();

		String SQL = "CREATE TABLE board (";
		SQL += "id INTEGER";
		SQL += ", name VARCHAR(20)";
		for (int i = 0; i < 25; i++) {
			SQL += ", value" + i + " VARCHAR(20)";
		}
		SQL += ", PRIMARY KEY(id))";
		handler.executeSQL(SQL);

	}

	if (isDrop) {
		DBHandler handler = new DBHandler();
		handler.executeSQL("DROP TABLE board");
	}

%>

<!DOCTYPE html>
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