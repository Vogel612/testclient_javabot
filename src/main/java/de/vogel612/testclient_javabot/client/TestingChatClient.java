package de.vogel612.testclient_javabot.client;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.gmail.inverseconduit.datatype.ChatMessage;

public class TestingChatClient {

	private final Map<Timestamp, ChatMessage> messages = new TreeMap<>();
	private Timestamp lastQueryTime;

	public List<ChatMessage> newMessages() {
		final List<ChatMessage> newMessages;
		synchronized (messages) {
			newMessages = messages.entrySet().stream()
					.filter((e) -> 0 < e.getKey().compareTo(lastQueryTime))
					.map(e -> e.getValue()).collect(Collectors.toList());
			lastQueryTime = new Timestamp(); // possibly just get the last
												// timestamp?
		}
		return newMessages;
	}

	public boolean newBotMessage(String message) {
		ChatMessage botMessage;
		synchronized (messages) {
			botMessage = BotMessageUtils.createFromString(message);
			messages.put(new Timestamp(botMessage.getMessageId()), botMessage);
		}
		notifyGui(botMessage);
		return true;
	}

	private void notifyGui(ChatMessage chatMessage) {

	}
}
