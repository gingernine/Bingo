function dispBoard() {
	/*
	 * ユーザー入力用のビンゴボードを作成する
	 */
	document.write("<table>");
	for (var i = 0; i < 5; i++) {
		document.write("<tr>");
		for (var j = 0; j < 5; j++) {
			document.write("<td>");
			if (i == 2 && j == 2) {
				document.write("<input type='text' id='" +
						"number" + (i * 5 + j) +
						"' class='numbers' value='B' readonly>");
			} else {
				document.write("<input type='text' id='" +
						"number" + (i * 5 + j) +
						"' class='numbers' value=''>");
			}
			document.write("</td>");
		}
		document.write("</tr>");
	}
	document.write("</table>");
}

$(function() {
	var ws; // WebSocketオブジェクトへの参照を格納
	var arrayFromBoard; // ビンゴボードの各セルに埋め込んだinputタグを取得

	var onMessage = function(event) {
		/*
		 * 今回はサーバーからはルーレットで出た数値しか送られてこない
		 * 送られてきた数値がビンゴボードにあるならば
		 * そのセルの背景色を変更する
		 */
		console.log("Number received from Servlet: " + event.data);
		for (var i = 0; i < arrayFromBoard.length; i++) {
			if (arrayFromBoard[i].value == event.data) {
				console.log("hit!");
				var id = "#number" + i;
				$(id).css("background", "red");
				break;
			}
		}
	};

	$("#connect").click(function() {
		ws = new WebSocket("ws://localhost:8080/Bingo/board");
		ws.onmessage = onMessage;

		$("#connect").attr("disabled", true);
		$("#send").attr("disabled", false);
		$("#random").attr("disabled", false);
		$("#disconnect").attr("disabled", false);

		$("#message").html("Input numbers into the board");
	});

	$("#send").click(function() {
		arrayFromBoard = document.getElementsByClassName("numbers");

		//入力数値のバリデーションチェックをする
		for (var i = 0; i < 25; i++) {
			/*for (!/[1-75/].test(arrayFromBoard[i].value)) {
				alert("1から75の数値を入力して下さい");
				flag = false;
				break;
			}*/
		}

		/*
		 * サーブレットにはテキストで送るので
		 * ビンゴボードの数値をカンマ区切りで連結する
		 */
		var msg = "";
		for (var i = 0; i < 25; i++) {
			msg += arrayFromBoard[i].value + ",";
		}
		ws.send(msg);

		$(".numbers").attr("readonly", true);
		$("#send").attr("disabled", true);
		$("#random").attr("disabled", true);

		$("#message").html("Roulette will start soon");
	});

	$("#random").click(function() {
		/*
		 * ユーザーが直接入力ではなく自動入力を選択した場合の処理
		 * ユーザーに代わってビンゴボードにランダムな数値を割り当てる
		 * ---数値の割り当て方---
		 * [1] 初めに1から75までの数値が入った配列(numarray)を作る
		 * [2] numarrayをFisher-Yates法によってシャッフルする
		 * [3] numarrayの初めの25要素をビンゴボードの数値とする
		 */
		var numarray = new Array(75);
		for (var i = 0; i < 75; i++) {
			numarray[i] = i + 1;
		}

		// Fisher-Yates method
		for (var i = numarray.length - 1; i > 0; i--) {
			var r = Math.floor(Math.random() * (i + 1));
			var tmp = numarray[i];
			numarray[i] = numarray[r];
			numarray[r] = tmp;
		}

		/*
		 * サーブレットにはテキストで送るので
		 * ビンゴボードの数値をカンマ区切りで連結する
		 */
		var msg = "";
		for (var i = 0; i < 25; i++) {
			msg += numarray[i] + ",";
			if (i == 12) {
				continue;
			}
			var id = "#number" + i;
			$(id).attr("value", numarray[i]);
		}

		$(".numbers").attr("readonly", true);
		$("#message").html("Click the Send button");
	});

	$("#disconnect").click(function() {
		for (var i = 0; i < 25; i++) {
			if (i == 12) {
				continue;
			}
			var id = "#number" + i;
			$(id).attr("value", "");
			$(id).attr("readonly", false);
		}
		$("#connect").attr("disabled", false);
		$("#send").attr("disabled", true);
		$("#random").attr("disabled", true);
		$("#disconnect").attr("disabled", true);

		$("#message").html("Thank you !");
		ws.close();
	});
});
