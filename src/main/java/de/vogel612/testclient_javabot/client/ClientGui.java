package de.vogel612.testclient_javabot.client;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import com.gmail.inverseconduit.chat.ChatWorker;
import com.gmail.inverseconduit.datatype.ChatMessage;

import de.vogel612.testclient_javabot.client.controller.ChatRenderController;

public class ClientGui extends Application implements ChatWorker {

	private static final Logger LOGGER = Logger.getLogger(ClientGui.class.getName());

	private ChatRenderController controller;
	private Stage stage;
	private static ClientGui instance;
	public static final CountDownLatch latch = new CountDownLatch(1);
	
	public static ClientGui getInstance() {
		return instance;
	}

	public ClientGui() {
		instance = this;
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		//Loading the FXML
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ChatRender.fxml"));
		BorderPane borderPane = (BorderPane) loader.load();
		controller = loader.getController();
		Scene scene = new Scene(borderPane);
		scene.getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());
		// Remove this to disable the dark theme
		scene.getStylesheets().add(getClass().getResource("/style/darkTheme.css").toExternalForm());
		stage.setScene(scene);
		latch.countDown();
	}

	/**
	 * {@see ChatWorker#start()}
	 */
	@Override
	public void start() {
		Platform.runLater(() -> {
			stage.show();
			controller.bindVvalue();
		});
	}

	/**
	 * {@see ChatWorker#enqueueMessage(ChatMessage)}
	 */
	@Override
	public boolean enqueueMessage(ChatMessage chatMessage) {
		Platform.runLater(() -> {
			try {
				controller.addMessage(chatMessage);
			} catch(Exception e) {
				LOGGER.log(Level.SEVERE, "Exception in adding message to chat window : ", e);
			}
		});
		return true;
	}
}