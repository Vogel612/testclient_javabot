package de.vogel612.testclient_javabot;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.gmail.inverseconduit.AppContext;
import com.gmail.inverseconduit.bot.DefaultBot;
import com.gmail.inverseconduit.chat.ChatInterface;
import com.gmail.inverseconduit.commands.sets.CoreBotCommands;

import de.vogel612.testclient_javabot.client.ClientGui;
import de.vogel612.testclient_javabot.core.TestingChatInterface;

/**
 * Class responsible for making things know each other. This class is the core
 * of the testclient. It's strongly oriented towards
 * {@link com.gmail.inverseconduit.bot.Program JavaBot's Program}, and basically
 * accomplishes the same tasks, just adapted to the needs of the testclient.
 * This includes firing up the {@link DefaultBot actual Bot} in JavaBot and
 * wiring it to the {@link TestingChatInterface}. Additionally the
 * {@link ClientGui} is started here and the {@link CommandHandle Commands} are added to the bot.
 * 
 * @author Vogel612<<a href="mailto:vogel612@gmx.de"
 *         >vogel612@gmx.de</a>>
 */
public class TestProgram {

	private static final Logger LOGGER = Logger.getLogger(TestProgram.class.getName());

	private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

	private final DefaultBot bot;

	private final ChatInterface chatInterface = new TestingChatInterface();

	private final ClientGui gui;

	public TestProgram() {
		LOGGER.info("instantiating TestProgram class");
		AppContext.INSTANCE.add(chatInterface);
		bot = new DefaultBot(chatInterface);
		gui = new ClientGui();
		chatInterface.subscribe(bot);
		chatInterface.subscribe(gui);
		LOGGER.info("Basic component setup completed, beginning command glueing.");
		new CoreBotCommands(chatInterface, bot).allCommands().forEach(bot::subscribe);
		LOGGER.info("Glued Core Commands");
	}

	public void startup() {
		// fake the login??
		scheduleQueryingThread();
		bot.start();
		gui.start();

		LOGGER.info("Startup successfully completed");
	}

	private void scheduleQueryingThread() {
		executor.scheduleAtFixedRate(() -> {
			try {
				chatInterface.queryMessages();
			} catch(Exception e) {
				Logger.getAnonymousLogger().severe("Exception in querying thread: " + e.getMessage());
				e.printStackTrace();
			}
		}, 3, 1, TimeUnit.SECONDS);
		Logger.getAnonymousLogger().info("querying thread started");
	}

	public void dispose() throws Exception {
		executor.shutdown();
	}

}
