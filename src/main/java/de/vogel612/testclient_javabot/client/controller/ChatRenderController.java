package de.vogel612.testclient_javabot.client.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import com.gmail.inverseconduit.datatype.ChatMessage;

import de.vogel612.testclient_javabot.client.custom.MessageBox;
import de.vogel612.testclient_javabot.core.ChatMessageUtils;
import de.vogel612.testclient_javabot.core.MessageTracker;

public class ChatRenderController implements Initializable {
	
	@FXML
	private BorderPane window;
	
	@FXML
	private Button submit;
	
	@FXML
	private TextArea userInput;
	
	@FXML
	private VBox chatWindow;
	
	@FXML 
	private HBox bottomSection;
	
	@FXML
	private ScrollPane chatWindowScroll;
	
	private MessageTracker chatClient;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		chatWindow.prefWidthProperty().bind(window.widthProperty());
		chatClient = MessageTracker.getInstance();
		chatWindowScroll.vvalueProperty().bind(chatWindow.heightProperty());
	}
	
	@FXML
	public void submit() {
		if(!userInput.getText().equals("")) {
			chatClient.newUserMessage(userInput.getText());
			userInput.clear();
			userInput.requestFocus();
		}
	}
	
	@FXML
	public void enterPressed(KeyEvent event) {
		if(event.getCode() == KeyCode.ENTER) {
			if (event.isShiftDown()) {
				// make a newline for it
				userInput.appendText("\r\n");
				return;
			}
			submit();
			event.consume();
		}
	}
	
	public void addMessage(ChatMessage chatMessage) {
		MessageBox messageBox = new MessageBox(ChatMessageUtils.createFromString(chatMessage.getMessage(), chatMessage.getUsername()));
		messageBox.prefWidthProperty().bind(window.widthProperty().subtract(50));
		chatWindow.getChildren().add(messageBox);
	}

}
