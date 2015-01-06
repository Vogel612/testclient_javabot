package de.vogel612.testclient_javabot.core;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.gmail.inverseconduit.datatype.ChatMessage;

public class TestingChatClient {

    private static final int               LIMIT         = 200;

    private static final TestingChatClient INSTANCE      = new TestingChatClient();

    private final ChatMessage[]            messages      = new ChatMessage[LIMIT];

    private final AtomicInteger            lastQueryTime = new AtomicInteger(0);

    private final AtomicInteger            currentItem   = new AtomicInteger(0);

    private TestingChatClient() {}

    private long getLimit() {
        return (LIMIT - lastQueryTime.get() + currentItem.get()) % LIMIT;
    }

    public List<ChatMessage> newMessages() {
        final List<ChatMessage> newMessages;
        final int start = lastQueryTime.get();
        final long limit = getLimit();
        synchronized (messages) {
            newMessages =
                    IntStream.generate(new Counter(start)).limit(limit).mapToObj(i -> messages[i]).filter(e -> e != null && !e.getMessage().isEmpty()).collect(Collectors.toList());
        }
        lastQueryTime.lazySet(currentItem.get());
        return newMessages;
    }

    public boolean newBotMessage(String message) {
        ChatMessage botMessage;
        botMessage = BotMessageUtils.createFromString(message, "Junior");
        incrementAndWrap();
        messages[currentItem.get()] = botMessage;
        return true;
    }

    public boolean newUserMessage(String message) {
        ChatMessage userMessage;
        userMessage = BotMessageUtils.createFromString(message, "You");
        incrementAndWrap();
        messages[currentItem.get()] = userMessage;
        return true;
    }

    private final class Counter implements IntSupplier {

        int current;

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

    private final void incrementAndWrap() {
        currentItem.incrementAndGet();
        currentItem.compareAndSet(LIMIT, 0);
    }

    public List<ChatMessage> getMessages() {
        return Arrays.asList(messages);
    }

    public static TestingChatClient getInstance() {
        return INSTANCE;
    }
}
