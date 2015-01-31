package de.vogel612.testclient_javabot;

import java.io.IOException;
import java.security.Policy;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import com.gmail.inverseconduit.AppContext;
import com.gmail.inverseconduit.BotConfig;
import com.gmail.inverseconduit.bot.Program;
import com.gmail.inverseconduit.chat.ChatInterface;
import com.gmail.inverseconduit.commands.sets.CoreBotCommands;
import com.gmail.inverseconduit.security.ScriptSecurityManager;
import com.gmail.inverseconduit.security.ScriptSecurityPolicy;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import de.vogel612.testclient_javabot.client.ClientGui;
import de.vogel612.testclient_javabot.core.TestingChatInterface;

/**
 * Entry point of Application. Class responsible for creating an JavaFX
 * Application thread
 * This class extends {@link Application}.
 */
public final class Main extends Application {

	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

	public static final void main(final String[] args) {
		setupLogging();

		Policy.setPolicy(ScriptSecurityPolicy.getInstance());
		System.setSecurityManager(ScriptSecurityManager.getInstance());

		BotConfig config = loadConfig();
		AppContext.INSTANCE.add(config);

		launch(Main.class);
	}

	private static BotConfig loadConfig() {
		Properties properties = new Properties();
		properties.setProperty("LOGIN-EMAIL", "");
		properties.setProperty("TRIGGER", "**");
		properties.setProperty("PASSWORD", "");
		properties.setProperty("ROOMS", "1");
		return new BotConfig(properties);
	}

	private static void setupLogging() {
		Filter filter = new Filter() {

			private final String packageName = Main.class.getPackage().getName();

			@Override
			public boolean isLoggable(final LogRecord record) {
				// only log messages from this app
				String name = record.getLoggerName();
				return name != null && name.startsWith(packageName);
			}
		};

		Logger global = Logger.getLogger("");
		for (Handler handler : global.getHandlers()) {
			handler.setFilter(filter);
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		ChatInterface chatInterface = new TestingChatInterface();
		ClientGui gui = new ClientGui();
		try {
			gui.init(primaryStage);
		} catch(IOException e) {
			chatInterface.close();
			throw new RuntimeException(e);
		}
		chatInterface.subscribe(gui);
		gui.start();

		Program program = new Program(chatInterface);

		//FIXME: Remove after merge of #41, because it's no more needed
		new CoreBotCommands(chatInterface, program.getBot()).allCommands().forEach(program.getBot()::subscribe);
		// Used to shut down the executor when the window is closed
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				chatInterface.getSubscriptions().forEach(worker -> {
					try {
						worker.enqueueMessage(Program.POISON_PILL);
					} catch(InterruptedException ignore) {}
				});
				LOGGER.info("Junior Client - Bye Bye!");
			}
		});

		scheduleQueryingThread(chatInterface);
		program.startup();
	}

	private void scheduleQueryingThread(ChatInterface chatInterface) {
		ThreadFactory factory =
				new ThreadFactoryBuilder().setDaemon(true).setNameFormat("message-query-thread-%d").build();
		Executors.newSingleThreadScheduledExecutor(factory).scheduleAtFixedRate(() -> queryMessagesFor(chatInterface),
			1000, 500, TimeUnit.MILLISECONDS);
		Logger.getAnonymousLogger().info("querying thread started");
	}

	private static void queryMessagesFor(ChatInterface chatInterface) {
		try {
			chatInterface.queryMessages();
		} catch(RuntimeException | Error e) {
			LOGGER.log(Level.SEVERE, "Runtime Exception or Error occurred:", e);
			throw e;
		} catch(Exception e) {
			LOGGER.log(Level.WARNING, "Exception occured:", e);
		}
	}
}
