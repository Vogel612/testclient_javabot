package de.vogel612.testclient_javabot.core;

import java.util.concurrent.atomic.AtomicLong;

import com.gmail.inverseconduit.SESite;
import com.gmail.inverseconduit.datatype.ChatMessage;

public class ChatMessageUtils {

	private static final AtomicLong generator = new AtomicLong(0);

	public static ChatMessage createFromString(final String message, final String username) {
		return new ChatMessage(SESite.STACK_OVERFLOW, 1, "offline-testing", username, 1, message, generator.incrementAndGet());
	}

}
