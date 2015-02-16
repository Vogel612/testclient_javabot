package de.vogel612.testclient_javabot.client.custom;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

import com.gmail.inverseconduit.datatype.ChatMessage;

public class MessageBox extends HBox {

	private final SVGPath triangle = new SVGPath();
	private final HBox box = new HBox();
	
	public MessageBox(final ChatMessage chatMessage, final DisplayFilter... displayFilter) {
		createTriangle();
		createContentBox(chatMessage, displayFilter);
		getChildren().addAll(triangle, box);
		if(chatMessage.getUsername().equalsIgnoreCase("You")){
			setSentLayout();
		} else {
			setReceivedLayout();
		}
		minHeight(30);
		getStyleClass().add("messageBox");
	}

	private void setSentLayout() {
		box.toBack();
		box.getStyleClass().add("sentMsg");
		setAlignment(Pos.CENTER_RIGHT);
		triangle.setRotate(180);
		triangle.setFill(Color.BLUEVIOLET);
	}
	
	private void setReceivedLayout() {
		box.toFront();
		box.getStyleClass().add("receivedMsg");
		setAlignment(Pos.CENTER_LEFT);
		triangle.setFill(Color.GOLDENROD);
	}
	
	private void createTriangle() {
		triangle.setContent("M 0 0 v 20 l -10 -10 z");
	}
	
	private void createContentBox(final ChatMessage chatMessage, final DisplayFilter... displayFilter) {
		final Label message = new Label();
		String messageText = chatMessage.getMessage();
		for (final DisplayFilter filter : displayFilter) {
			messageText = filter.filter(messageText);
		}
		message.setText(messageText);
		message.setWrapText(true);
		box.getChildren().addAll(message);
	}
	
}
