package de.vogel612.testclient_javabot.client;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import com.gmail.inverseconduit.bot.Program;
import com.gmail.inverseconduit.chat.ChatWorker;
import com.gmail.inverseconduit.datatype.ChatMessage;

import de.vogel612.testclient_javabot.client.controller.ChatRenderController;

/**
 * Class responsible for loading the FXML and creating an JavaFX test client
 * scene
 * for the JavaBot.
 * 
 * @author itachi<<a href="mailto:abhinay_agarwal@live.com"
 *         >abhinay_agarwal@live.com</a>>
 */
public class ClientGui implements ChatWorker {

	private static final Logger LOGGER = Logger.getLogger(ClientGui.class.getName());

	private ChatRenderController controller;
	private Stage stage;

	/**
	 * Loads FXML and css, creates a scene and plugs it into the stage <br/>
	 * 
	 * @throws IOException
	 */
	public void init(Stage stage) throws IOException {
		LOGGER.info("Loading fxml and css");
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
		LOGGER.info("ClientGui loaded successfully");
	}

	/**
	 * {@see ChatWorker#start()}
	 */
	@Override
	public void start() {
		stage.show();
		controller.bindVvalue();
	}

	/**
	 * {@see ChatWorker#enqueueMessage(ChatMessage)}
	 */
	@Override
	public boolean enqueueMessage(ChatMessage chatMessage) {
		if (chatMessage == Program.POISON_PILL) {
			Platform.exit();
		}

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
