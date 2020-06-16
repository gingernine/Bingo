package bingo;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MyDecoder implements Decoder.Text<ReceivedMessage> {

	@Override public void init(EndpointConfig config) {/*処理なし*/}

	@Override
	public boolean willDecode(String s) {
		System.out.println("willDecode");
		return true;
	}

	@Override
	public ReceivedMessage decode(String s) throws DecodeException {
		System.out.println("decode");

		String[] tokens = s.split(";");
		String[] subtokens1 = tokens[0].split(":");
		String[] subtokens2 = tokens[1].split(":");

		ReceivedMessage rm = new ReceivedMessage();
		rm.name = subtokens1[1];
		rm.values = subtokens2[1];

		return rm;
	}

	@Override public void destroy() {/*処理なし*/}
}
