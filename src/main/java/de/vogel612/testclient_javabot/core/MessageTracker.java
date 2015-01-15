package de.vogel612.testclient_javabot.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.gmail.inverseconduit.datatype.ChatMessage;

/*
 * This file is exempt from the Project's general License clause and instead
 * Licensed under the Creative Commons Attribution ShareAlike License, Version
 * 4.0 (The "CC-License")
 * The CC-License is obtainable from
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode
 * and additionally available in the file "CC-BY-SA_License.txt"
 */
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
 * given.<br/>
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

	/**
	 * Returns the up to 200 latest messages in chronological order. If less
	 * than 200 messages are in the Tracker, only these messages are returned. <br/>
	 * <br/>
	 * Empty Messages are ignored.
	 * 
	 * @return an unmodifiable List of messages
	 */
	public List<ChatMessage> getMessages() {
		synchronized (circularBuffer) {
			// let the system figure it out
			return newMessages(currentMessage - CAPACITY);
		}
	}

	/**
	 * Returns the messages that were added since the last time <i>any</i> of
	 * {@link #getMessages()}, {@link #newMessages(long)} or
	 * {@link #newMessages()} was called in chronological order.
	 * The absolute maximum number of returned messages is 200.<br/>
	 * <br/>
	 * Empty Messages are ignored.
	 * 
	 * @return an unmodifiable List of messages
	 */
	public List<ChatMessage> newMessages() {
		synchronized (circularBuffer) {
			return newMessages(lastMessageReported);
		}
	}

	/**
	 * Returns the messages since a given message in chronological order. If
	 * more than 200 messages were added since the given message, the count of
	 * messages is reduced by 200 until less than 200 messages remain for
	 * returning.<br/>
	 * This means: <br/>
	 * Assuming there were <b>450</b> messages added to the Tracker, calling
	 * this
	 * method with since as <b>200</b> returns a list of <b>50</b> messages.<br/>
	 * <br/>
	 * Empty Messages are ignored in the resultset, but not in the calculation
	 * of the number of messages to return.<br/>
	 * Assuming message <b>400</b> through to <b>420</b> were empty, the result
	 * only contains
	 * <b>30</b> messages.
	 * 
	 * @param since
	 *        the long "identifier" of the message to take as starting point
	 * @return an unmodifiable List of messages
	 */
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

	/**
	 * Adds a {@link ChatMessage} to the MessageTracker. Messages lieing back
	 * over 200
	 * messages get irredeemably overwritten. Adding null overwrites the oldest
	 * message.
	 * 
	 * @param message
	 *        the message to be added. May be null
	 * @return a boolean indicating success or failure
	 */
	//FIXME why is this returning a boolean? make it return currentMessage!
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

	/**
	 * Allows adding a new message, pretending it's coming from the Bot. It will
	 * be assigned the username "Junior"
	 * 
	 * @param message
	 *        a String containing the message text
	 * @return a boolean indicating success or failure
	 */
	//FIXME: boolean return? state-machine!
	public boolean newBotMessage(String message) {
		return addMessage(ChatMessageUtils.createFromString(message, "Junior"));
	}

	/**
	 * Allows adding a new message coming from the User. It will be assigned the
	 * Username "you"
	 * 
	 * @param message
	 *        a String containing the message text
	 * @return a boolean indicating success or failure
	 */
	//FIXME: boolean return yet again!
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

	/**
	 * Grabs the available instance of MessageTracker.
	 * 
	 * @return the only available MessageTracker
	 */
	public static MessageTracker getInstance() {
		return INSTANCE;
	}
}
