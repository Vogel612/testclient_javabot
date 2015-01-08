package de.vogel612.testclient_javabot.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.gmail.inverseconduit.datatype.ChatMessage;

import de.vogel612.testclient_javabot.core.MessageTracker;

public class MessageTrackerTests {

    private static final String TEST_MESSAGE       = "TestMessage";

    private static final String OTHER_TEST_MESSAGE = "Some Test Message";

    private MessageTracker   cut                = MessageTracker.getInstance();

    @Before
    public void setup() {
        cut.reset();
    }

    @Test
    public void newMessagesDoesntHaveNewMessages() {
        List<ChatMessage> actual = cut.newMessages();

        assertTrue(actual.isEmpty());
    }

    @Test
    public void newMessagesReturnsSingleMessage() {
        cut.newUserMessage(TEST_MESSAGE);

        List<ChatMessage> actual = cut.newMessages();

        assertTrue(actual.size() == 1);
        assertEquals(TEST_MESSAGE, actual.get(0).getMessage());
    }

    @Test
    public void newMessagesConsumesSingleMessage() {
        cut.newUserMessage(TEST_MESSAGE);

        cut.newMessages();
        List<ChatMessage> actual = cut.newMessages();

        assertTrue(actual.isEmpty());
    }

    @Test
    public void newMessagesReturnsMultipleMessages() {
        cut.newUserMessage(TEST_MESSAGE);
        cut.newUserMessage(OTHER_TEST_MESSAGE);

        List<ChatMessage> actual = cut.newMessages();

        assertTrue(actual.size() == 2);
        assertEquals(TEST_MESSAGE, actual.get(0).getMessage());
        assertEquals(OTHER_TEST_MESSAGE, actual.get(1).getMessage());
    }

    @Test
    public void newMessagesOnlyHidesSingleMessage() {
        cut.newUserMessage(TEST_MESSAGE);

        cut.newMessages();
        List<ChatMessage> actual = cut.newMessages(0);

        assertTrue(actual.size() == 1);
        assertEquals(TEST_MESSAGE, actual.get(0).getMessage());
    }

    @Test
    public void newMessagesIgnoresEmptyMessages() {
        cut.newUserMessage("");

        List<ChatMessage> actual = cut.newMessages();

        assertTrue(actual.isEmpty());
    }

    @Test
    public void newMessagesWrapsAroundAndIgnoresNullMessages() {
        cut.newUserMessage(TEST_MESSAGE);

        List<ChatMessage> actual = cut.newMessages(150);

        assertTrue(actual.size() == 1);
        assertEquals(TEST_MESSAGE, actual.get(0).getMessage());
    }

    @Test
    public void newUserMessageHasCorrectUsername() {
        cut.newUserMessage(TEST_MESSAGE);

        List<ChatMessage> actual = cut.newMessages();

        assertTrue(actual.size() == 1);
        assertEquals("You", actual.get(0).getUsername());
    }

    @Test
    public void newBotMessageHasCorrectUsername() {
        cut.newBotMessage(TEST_MESSAGE);

        List<ChatMessage> actual = cut.newMessages();

        assertTrue(actual.size() == 1);
        assertEquals("Junior", actual.get(0).getUsername());
    }
}
