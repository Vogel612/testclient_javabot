package de.vogel612.testclient_javabot.client;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.gmail.inverseconduit.chat.ChatWorker;
import com.gmail.inverseconduit.datatype.ChatMessage;

import de.vogel612.testclient_javabot.client.controller.ChatRenderController;

public class ClientGui implements ChatWorker {
	
	private static final Logger LOGGER = Logger.getLogger(ClientGui.class.getName());
	
	private ChatRenderController controller;
	private JFrame frame;
	final CountDownLatch latch = new CountDownLatch(1);

	public void initFX(JFXPanel fxPanel) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(
	               "/de/vogel612/testclient_javabot/client/fxml/ChatRender.fxml"));
		BorderPane borderPane = (BorderPane)loader.load();
		controller = loader.getController();
		Scene scene = new Scene(borderPane);
		scene.getStylesheets().add(getClass().getResource("/de/vogel612/testclient_javabot/client/style/style.css").toExternalForm());
		// Remove this to disable the dark theme
		scene.getStylesheets().add(getClass().getResource("/de/vogel612/testclient_javabot/client/style/darkTheme.css").toExternalForm());
		fxPanel.setScene(scene);
	}
	
	public ClientGui() {
		try{
			SwingUtilities.invokeLater(new Runnable() {
	            @Override
	            public void run() {
	            	frame = new JFrame("Swing and JavaFX");
	                final JFXPanel fxPanel = new JFXPanel();
	                frame.add(fxPanel);
	                frame.setSize(550, 421);
	                frame.setVisible(true);
	                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	                Platform.runLater(new Runnable() {
	                    @Override
	                    public void run() {
	                        try {
								initFX(fxPanel);
								latch.countDown();
							} catch (Exception e) {
								LOGGER.log(Level.SEVERE, "Exception in creating JavaFX thread", e);
							}
	                    }
	               });
	            }
	        });
			latch.await();
		} catch (InterruptedException iep) {
			LOGGER.log(Level.SEVERE, "Exception in waiting for Latch", iep);
		}
	}
	
	public void start() {
		frame.setVisible(true);
	}

	@Override
	public boolean enqueueMessage(ChatMessage chatMessage)
			throws InterruptedException {
		Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                	controller.addMessage(chatMessage);
                } catch(Exception e) {
                	LOGGER.log(Level.SEVERE, "Exception in adding message to chat window : ", e);
                }
            }
        });
		return true;
	}

}
