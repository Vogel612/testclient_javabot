package de.vogel612.testclient_javabot.core;

import java.util.concurrent.atomic.AtomicLong;

import com.gmail.inverseconduit.SESite;
import com.gmail.inverseconduit.datatype.ChatMessage;

/**
 * This class is designed to give a way to generate simple {@link ChatMessage
 * ChatMessages} by providing a minimum of required data.
 * All generated messages are automatically assigned subsequent long messageIds. <br/>
 * <br/>
 * The class is safe for use by multiple threads
 * 
 * @author Vogel612<<a href="mailto:vogel612@gmx.de"
 *         >vogel612@gmx.de</a>>
 */
public final class ChatMessageUtils {

	private static final AtomicLong generator = new AtomicLong(0);

	public static ChatMessage createFromString(final String message, final String username) {
		return new ChatMessage(SESite.STACK_OVERFLOW, 1, "offline-testing", username, 1, message, generator.incrementAndGet());
	}

}
