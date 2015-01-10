package de.vogel612.testclient_javabot.client;

import java.util.concurrent.CountDownLatch;

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
import de.vogel612.testclient_javabot.core.ChatMessageUtils;

public class ClientGui implements ChatWorker {
	
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
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	                    }
	               });
	            }
	        });
			latch.await();
		} catch (InterruptedException iep) {
			//Add Logging
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
                	
                }
            }
        });
		return true;
	}

}
