package de.vogel612.testclient_javabot.core;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmail.inverseconduit.chat.ChatInterface;
import com.gmail.inverseconduit.chat.ChatWorker;
import com.gmail.inverseconduit.datatype.ChatDescriptor;
import com.gmail.inverseconduit.datatype.CredentialsProvider;
import com.gmail.inverseconduit.datatype.ProviderDescriptor;

/**
 * Our Custom implementation of the JavaBot's {@link ChatInterface}. The only
 * effectively implemented methods are: {@link #subscribe(ChatWorker) subscribe}
 * , {@link #unSubscribe(ChatWorker) unSubscribe}, {@link #queryMessages()
 * queryMessages}, {@link #sendMessage(ChatDescriptor, String) sendMessage},
 * {@link #broadcast(String) broadcast} and {@link #getSubscriptions()
 * getSubscriptions}.
 * All other methods return default or "success" values
 * 
 * @author Vogel612<<a href="mailto:vogel612@gmx.de"
 *         >vogel612@gmx.de</a>>
 */
public class TestingChatInterface implements ChatInterface {

	private static final Logger LOGGER = Logger.getLogger(TestingChatInterface.class.getName());

	private final Set<ChatWorker> subscribers = new HashSet<>();

	private final MessageTracker fakeClient = MessageTracker.getInstance();

	@Override
	public void subscribe(final ChatWorker subscriber) {
		subscribers.add(subscriber);
	}

	@Override
	public void unSubscribe(final ChatWorker subscriber) {
		subscribers.remove(subscriber);
	}

	@Override
	public void queryMessages() {
		fakeClient.newMessages().forEach(m -> {
			subscribers.forEach(s -> {
				try {
					s.enqueueMessage(m);
				} catch(Exception e) {
					LOGGER.log(Level.SEVERE, "Exception when enqueueing a message", e);
				}
			});
		});
	}

	@Override
	public boolean sendMessage(final ChatDescriptor descriptor, final String message) {
		return fakeClient.newBotMessage(message);
	}

	@Override
	public boolean joinChat(final ChatDescriptor descriptor) {
		return true;
	}

	@Override
	public boolean leaveChat(final ChatDescriptor descriptor) {
		return true;
	}

	@Override
	public boolean login(final ProviderDescriptor descriptor, final CredentialsProvider credentials) {
		return true;
	}

	@Override
	public void broadcast(final String message) {
		fakeClient.newBotMessage(message);
	}

	@Override
	public Collection<ChatWorker> getSubscriptions() {
		return Collections.unmodifiableCollection(subscribers);
	}
}
