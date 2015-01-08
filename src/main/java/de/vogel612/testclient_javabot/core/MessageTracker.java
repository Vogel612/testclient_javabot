package de.vogel612.testclient_javabot.core;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.gmail.inverseconduit.datatype.ChatMessage;

public final class MessageTracker {

    private static final int               LIMIT         = 200;

    private static final MessageTracker INSTANCE      = new MessageTracker();

    private final ChatMessage[]            messages      = new ChatMessage[LIMIT];

    private final AtomicInteger            lastQueryTime = new AtomicInteger(0);

    private final AtomicInteger            currentItem   = new AtomicInteger(0);

    private MessageTracker() {}

    private long getLimit(final int from) {
        return (LIMIT - from + currentItem.get()) % LIMIT;
    }

    public List<ChatMessage> newMessages() {
        return newMessages(lastQueryTime.get());
    }

    public List<ChatMessage> newMessages(final int since) {
        final List<ChatMessage> newMessages;
        final long limit = getLimit(since);
        synchronized (messages) {
            newMessages =
                    IntStream.generate(new Counter(since)).limit(limit).mapToObj(i -> messages[i]).filter(e -> e != null && !e.getMessage().isEmpty()).collect(Collectors.toList());
        }
        lastQueryTime.lazySet(currentItem.get());
        return newMessages;
    }

    public boolean newBotMessage(String message) {
        ChatMessage botMessage;
        botMessage = ChatMessageUtils.createFromString(message, "Junior");
        incrementAndWrap();
        messages[currentItem.get()] = botMessage;
        return true;
    }

    public boolean newUserMessage(String message) {
        ChatMessage userMessage;
        userMessage = ChatMessageUtils.createFromString(message, "You");
        incrementAndWrap();
        messages[currentItem.get()] = userMessage;
        return true;
    }

    private final class Counter implements IntSupplier {

        private int current;

        Counter(int start) {
            current = start;
        }

        @Override
        public int getAsInt() {
            current = incrementAndWrap(current);
            return current;
        }

        private final int incrementAndWrap(int item) {
            item++ ;
            if (item == LIMIT) {
                item = 0;
            }
            return item;
        }
    }

    /**
     * Allows resetting the TestingChatClient. All currently stored messages
     * will be lost.
     */
    public void reset() {
        currentItem.lazySet(0);
        lastQueryTime.lazySet(0);
        // no need to reset messages ;)
    }

    private final void incrementAndWrap() {
        currentItem.incrementAndGet();
        currentItem.compareAndSet(LIMIT, 0);
    }

    public List<ChatMessage> getMessages() {
        return Arrays.asList(messages);
    }

    public static MessageTracker getInstance() {
        return INSTANCE;
    }
}
