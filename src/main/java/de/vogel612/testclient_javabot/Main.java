package de.vogel612.testclient_javabot;

import java.security.Policy;
import java.util.Properties;
import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.gmail.inverseconduit.AppContext;
import com.gmail.inverseconduit.BotConfig;
import com.gmail.inverseconduit.security.ScriptSecurityManager;
import com.gmail.inverseconduit.security.ScriptSecurityPolicy;
import javafx.application.Application;
import javafx.stage.Stage;

public final class Main extends Application {

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
				return (name == null)
					? false
					: name.startsWith(packageName);
			}
		};

		Logger global = Logger.getLogger("");
		for (Handler handler : global.getHandlers()) {
			handler.setFilter(filter);
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		TestProgram p = new TestProgram(primaryStage);
		AppContext.INSTANCE.add(p);

		p.startup();
	}
}
