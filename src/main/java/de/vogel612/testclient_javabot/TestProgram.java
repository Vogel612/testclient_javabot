package de.vogel612.testclient_javabot;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.gmail.inverseconduit.bot.DefaultBot;
import com.gmail.inverseconduit.chat.ChatInterface;
import com.gmail.inverseconduit.commands.sets.CoreBotCommands;

import de.vogel612.testclient_javabot.core.TestingChatInterface;

public class TestProgram {

	private static final Logger LOGGER = Logger.getLogger(TestProgram.class
			.getName());
	private static final ScheduledExecutorService executor = Executors
			.newSingleThreadScheduledExecutor();
	private final DefaultBot bot;

	private final ChatInterface chatInterface = new TestingChatInterface();

	public TestProgram() {
		LOGGER.info("instantiating TestProgram class");
		bot = new DefaultBot(chatInterface);
		chatInterface.subscribe(bot);
		LOGGER.info("Basic component setup completed, beginning command glueing.");
		new CoreBotCommands(chatInterface).allCommands()
				.forEach(bot::subscribe);
		LOGGER.info("Glued Core Commands");
	}

	public void startup() {
		// fake the login??
		scheduleQueryingThread();
		bot.start();
		LOGGER.info("Startup successfully completed");
	}

	private void scheduleQueryingThread() {
		executor.scheduleAtFixedRate(
				() -> {
					try {
						chatInterface.queryMessages();
					} catch (Exception e) {
						Logger.getAnonymousLogger().severe(
								"Exception in querying thread: "
										+ e.getMessage());
						e.printStackTrace();
					}
				}, 5, 3, TimeUnit.SECONDS);
		Logger.getAnonymousLogger().info("querying thread started");
	}

}
