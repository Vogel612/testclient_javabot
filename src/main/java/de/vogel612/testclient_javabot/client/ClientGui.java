package de.vogel612.testclient_javabot.client;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.AdjustmentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import com.gmail.inverseconduit.chat.ChatWorker;
import com.gmail.inverseconduit.datatype.ChatMessage;

import de.vogel612.testclient_javabot.client.table.ChatCellRenderer;
import de.vogel612.testclient_javabot.client.table.ChatTableModel;
import de.vogel612.testclient_javabot.core.MessageTracker;

public class ClientGui extends JFrame implements ChatWorker {

    private static final Insets  STANDARD_PADDING = new Insets(10, 10, 10, 10);

    //TODO: do we even need this? maybe going through ChatInterface is more favorable
    private final MessageTracker chatClient       = MessageTracker.getInstance();

    private final ChatTableModel tableModel       = new ChatTableModel();

    private final JTable         messageTable;

    private final JScrollPane    messageView;

    private final JTextArea      userInput        = new JTextArea(3, 250);

    private final JButton        submit           = new JButton("send");

    public ClientGui() {
        messageTable = new JTable(tableModel);
        messageView = new JScrollPane(messageTable);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new GridBagLayout());
        this.setMinimumSize(new Dimension(350, 500));

        setupHeading();
        setupMessageView();
        setupUserInputArea();

        this.doLayout();
    }

    private void setupUserInputArea() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 2;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 0.0;
        c.weightx = 0.8;
        c.insets = STANDARD_PADDING;
        JScrollPane inputContainer = new JScrollPane(userInput, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        inputContainer.setMinimumSize(new Dimension(230, 50));
        this.add(inputContainer, c);
        c.weightx = 0.2;
        submit.setMinimumSize(new Dimension(100, 40));
        this.add(submit, c);
        bindListeners();
    }

    private void setupMessageView() {
        setupMessageTable();
        messageView.setAutoscrolls(true);
        //Following line is adapted from http://stackoverflow.com/a/15784385/1803692
        messageView.getVerticalScrollBar().addAdjustmentListener((AdjustmentEvent e) -> e.getAdjustable().setValue(e.getAdjustable().getMaximum()));
        messageView.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        messageView.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 1;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = STANDARD_PADDING;
        this.add(messageView, c);
    }

    private void setupHeading() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = 2;
        c.gridy = 0;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.insets = STANDARD_PADDING;
        this.add(new JLabel("Testing site for JavaBot aka. Junior"), c);
    }

    private void setupMessageTable() {
        messageTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        messageTable.setAutoCreateColumnsFromModel(true);
        messageTable.setFillsViewportHeight(true);
        messageTable.setDefaultRenderer(ChatMessage.class, new ChatCellRenderer());
        messageTable.setVisible(true);
    }

    private void bindListeners() {
        userInput.addKeyListener(new InputListener());
        submit.addActionListener(action -> {
            chatClient.newUserMessage(userInput.getText());
            userInput.setText("");
            userInput.requestFocus();
        });
    }

    @Override
    public void start() {
        this.setVisible(true);
    }

    @Override
    public boolean enqueueMessage(final ChatMessage chatMessage) {
        tableModel.addNewMessage(chatMessage);
        return true;
    }

    private final class InputListener implements KeyListener {

        @Override
        public void keyPressed(KeyEvent ev) {
            if (ev.getKeyCode() == KeyEvent.VK_ENTER) {
                if (ev.isShiftDown()) {
                    // make a newline for it
                    userInput.append("\r\n");
                    return;
                }
                ev.consume();
                submit.doClick();
            }
        }

        @Override
        public void keyReleased(KeyEvent ev) {
            // do nothing
        }

        @Override
        public void keyTyped(KeyEvent ev) {
            //do nothing
        }
    }
}
