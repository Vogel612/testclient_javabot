package de.vogel612.testclient_javabot.core;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmail.inverseconduit.chat.ChatInterface;
import com.gmail.inverseconduit.chat.ChatWorker;
import com.gmail.inverseconduit.datatype.ChatDescriptor;
import com.gmail.inverseconduit.datatype.CredentialsProvider;
import com.gmail.inverseconduit.datatype.ProviderDescriptor;

import de.vogel612.testclient_javabot.client.TestingChatClient;

public class TestingChatInterface implements ChatInterface {

	private static final Logger LOGGER = Logger
			.getLogger(TestingChatInterface.class.getName());

	private final Set<ChatWorker> subscribers = new HashSet<>();

	private final TestingChatClient client = new TestingChatClient();

	public void subscribe(ChatWorker subscriber) {
		subscribers.add(subscriber);
	}

	public void unSubscribe(ChatWorker subscriber) {
		subscribers.remove(subscriber);
	}

	public void queryMessages() {
		client.newMessages().forEach(
				m -> subscribers.forEach(s -> {
					try {
						s.enqueueMessage(m);
					} catch (Exception e) {
						LOGGER.log(Level.SEVERE,
								"Exception when enqueueing a message", e);
					}
				}));
	}

	public boolean sendMessage(ChatDescriptor descriptor, String message) {
		return client.newBotMessage(message);
	}

	public boolean joinChat(ChatDescriptor descriptor) {
		return true;
	}

	public boolean leaveChat(ChatDescriptor descriptor) {
		return true;
	}

	public boolean login(ProviderDescriptor descriptor,
			CredentialsProvider credentials) {
		return true;
	}

	public void broadcast(String message) {
		client.newBotMessage(message);
	}
}
