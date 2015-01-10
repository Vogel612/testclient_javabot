package de.vogel612.testclient_javabot.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.gmail.inverseconduit.datatype.ChatMessage;

/**
 * Class keeping track of the messages currently in the system. This class can
 * hold up to 200 messages in a circular buffer. Any more added messages
 * overwrite older messages.<br/>
 * This class has been under review at <a
 * href="http://codereview.stackexchange.com/questions/77127">Codereview SE</a><br/>
 * Special thanks to CodeReview User <a
 * href="http://codereview.stackexchange.com/users/31503/rolfl">rolfl</a>, for
 * finding multiple bugs and
 * providing a wonderful alternative. <br/>
 * <br/>
 * The code following is an adaption of the excellent <a
 * href="http://codereview.stackexchange.com/a/77136/37660">answer</a> he as
 * given.
 * 
 * @author Vogel612<<a href="mailto:vogel612@gmx.de"
 *         >vogel612@gmx.de</a>>
 */
public final class MessageTracker {

	private static final MessageTracker INSTANCE = new MessageTracker();

	private static final int CAPACITY = 200;
	private final ChatMessage[] circularBuffer = new ChatMessage[CAPACITY];

	private long lastMessageReported = 0;
	private long currentMessage = 0;
	private int size = 0;

	private MessageTracker() {}

	public List<ChatMessage> getMessages() {
		synchronized (circularBuffer) {
			// let the system figure it out
			return newMessages(currentMessage - CAPACITY);
		}
	}

	public List<ChatMessage> newMessages() {
		synchronized (circularBuffer) {
			return newMessages(lastMessageReported);
		}
	}

	public List<ChatMessage> newMessages(final long since) {
		if (since < 0) {
			// correct broken input with recursion
			return newMessages(0l);
		}
		synchronized (circularBuffer) {
			if (since >= currentMessage) { return Collections.emptyList(); }
			final int reportCount = (int) Math.min(size, currentMessage - since);
			final List<ChatMessage> result = new ArrayList<>(reportCount);

			int offset = (int) ( (currentMessage - reportCount + 1) % CAPACITY);
			for (int i = 0; i < reportCount; i++ ) {
				result.add(circularBuffer[offset]);
				offset = (offset + 1) % CAPACITY;
			}
			// remove unhelpful messages from the results 
			result.removeIf(message -> message == null || message.getMessage().isEmpty());

			lastMessageReported = currentMessage;
			return Collections.unmodifiableList(result);
		}
	}

	private boolean addMessage(final ChatMessage message) {
		synchronized (circularBuffer) {
			currentMessage++ ;
			circularBuffer[(int) (currentMessage % CAPACITY)] = message;
			if (size < CAPACITY) {
				size++ ;
			}
		}
		return true;
	}

	public boolean newBotMessage(String message) {
		return addMessage(ChatMessageUtils.createFromString(message, "Junior"));
	}

	public boolean newUserMessage(String message) {
		return addMessage(ChatMessageUtils.createFromString(message, "You"));
	}

	/**
	 * Allows resetting the TestingChatClient. All currently stored messages
	 * will be lost.
	 */
	public void reset() {
		currentMessage = 0;
		lastMessageReported = 0;
		Arrays.fill(circularBuffer, null);
	}

	public static MessageTracker getInstance() {
		return INSTANCE;
	}
}
