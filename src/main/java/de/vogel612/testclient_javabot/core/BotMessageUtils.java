package de.vogel612.testclient_javabot.core;

import com.gmail.inverseconduit.SESite;
import com.gmail.inverseconduit.datatype.ChatMessage;

public class BotMessageUtils {

	public static ChatMessage createFromString(String message, String username) {
		Timestamp stamp = new Timestamp();

		return new ChatMessage(SESite.STACK_OVERFLOW, 1, "offline-testing",
				username, 1, message, stamp.asLong());
	}

}
