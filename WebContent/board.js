$(function() {
	var ws; // WebSocketオブジェクトへの参照を格納
	var username; // ユーザーネームを格納
	var arrayOfBoardNums = new Array(25); // ビンゴボードの各セルの数値を保持（配列）

	function separator(text) {

		var obj = {};

		var tokens = text.split(";");
		var subtokens1 = tokens[0].split(":");
		var subtokens2 = tokens[1].split(":");

		obj.message = subtokens1[1];
		obj.num = subtokens2[1];

		return obj;
	}

	var onMessage = function(event) {
		/*
		 * 今回はサーバーからはルーレットで出た数値しか送られてこない
		 * 送られてきた数値がビンゴボードにあるならば
		 * そのセルの背景色を変更する
		 */
		console.log("Number received from Servlet: " + event.data);
		var d = separator(event.data);

		$("#message").html(d.num + d.message);

		for (var i = 0; i < 25; i++) {
			if (arrayOfBoardNums[i] == d.num) {
				console.log("hit!");
				var id = "#number" + i;
				$(id).css("background", "red");
				break;
			}
		}
	};

	$("#connect").click(function() {
		if ($("#username").val() == "") {
			alert("名前を入力して下さい");
			return;
		}

		username = $("#username").val();
		$("#username").attr("readonly", true);

		ws = new WebSocket("ws://localhost:8080/Bingo/wsservlet");
		ws.onmessage = onMessage;

		$("#connect").attr("disabled", true);
		$("#send").attr("disabled", false);
		$("#auto").attr("disabled", false);
		$("#disconnect").attr("disabled", false);

		$("#message").html("Input numbers into the board");
	});

	$("#send").click(function() {
		var arrayOfInputTags = document.getElementsByClassName("card"); // ビンゴボードの各セルに埋め込んだinputタグを保持（配列）
		for (var i = 0; i < 25; i++) {
			arrayOfBoardNums[i] = arrayOfInputTags[i].value;
		}

		console.log(arrayOfBoardNums);

		/*
		 * 入力数値のバリデーションチェック
		 * 1から75までの数値に一致しなければ警告画面を表示する
		 */
		const regex1 = RegExp('^[1-9]$');
		const regex2 = RegExp('^[1-6][0-9]$');
		const regex3 = RegExp('^7[0-5]$');

		for (var i = 0; i < 25; i++) {
			if (i == 12) {
				continue;
			}

			if (!regex1.test(arrayOfBoardNums[i]) &&
					!regex2.test(arrayOfBoardNums[i]) &&
					!regex3.test(arrayOfBoardNums[i])) {
				$("#alert").html("1から75の数値を入力して下さい");
				$("#alert").css("color", "red");
				return;
			}
		}

		/*
		 * 入力値に重複があれば警告画面を表示する
		 */
		var s = new Set(arrayOfBoardNums);
		if (s.size != 25) {
			$("#alert").html("数値に重複があります");
			$("#alert").css("color", "red");
			return
		}

		$("#alert").html("");

		var msg = {
				username : username,
				values : arrayOfBoardNums
		};

		ws.send(JSON.stringify(msg));

		$("#number12").css("background", "red");

		$(".card").attr("readonly", true);
		$("#send").attr("disabled", true);
		$("#auto").attr("disabled", true);

		$("#message").html("Roulette will start soon");
	});

	$("#auto").click(function() {
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
			$(id).val(numarray[i]);
		}

		$("#alert").html("");
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
		$("#auto").attr("disabled", true);
		$("#disconnect").attr("disabled", true);

		$("#message").html("Thank you !");
		ws.close();
	});
});
