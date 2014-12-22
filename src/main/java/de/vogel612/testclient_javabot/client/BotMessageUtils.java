package de.vogel612.testclient_javabot.client;

import com.gmail.inverseconduit.SESite;
import com.gmail.inverseconduit.datatype.ChatMessage;

public class BotMessageUtils {

	private static final String USERNAME = "Junior";

	public static ChatMessage createFromString(String message) {
		Timestamp stamp = new Timestamp();

		return new ChatMessage(SESite.STACK_OVERFLOW, 1, "offline-testing",
				USERNAME, 1, message, stamp.asLong());
	}

}
