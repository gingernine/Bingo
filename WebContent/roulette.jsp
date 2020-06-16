<%@page language="java" contentType="text/html" pageEncoding="utf-8"
	import="board1.WSServlet"
	import="board1.Roulette"
	import="java.util.ArrayList"
	import="java.util.Random"
	import="javax.websocket.Session"%>
<!DOCTYPE html>
<%
	boolean isPOST = request.getMethod().toLowerCase().equals("post");
	ArrayList<Integer> roulette = Roulette.getRouletteList();

	if (isPOST) {
		Roulette.sendResOfRoulette();
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