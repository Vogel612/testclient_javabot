package de.vogel612.testclient_javabot.client;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.gmail.inverseconduit.chat.ChatWorker;
import com.gmail.inverseconduit.datatype.ChatMessage;

import de.vogel612.testclient_javabot.core.TestingChatClient;

public class ClientGui extends JFrame implements ChatWorker {

    private final TestingChatClient chatClient = TestingChatClient.getInstance();

    private final ChatTableModel    tableModel = new ChatTableModel();

    private final JTable            messageTable;

    private final JScrollPane       messageView;

    private final JTextField        userInput  = new JTextField(250);

    private final JButton           submit     = new JButton("send");

    public ClientGui() {
        messageTable = new JTable(tableModel);
        setupMessageTable();
        messageView = new JScrollPane(messageTable);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = 2;
        c.gridy = 0;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 1.0;
        c.weighty = 0.1;
        this.add(new JLabel("Testing site for JavaBot aka. Junior"), c);

        c = new GridBagConstraints();
        c.gridy = 1;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        this.add(messageView, c);

        c = new GridBagConstraints();
        c.gridy = 2;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 0.1;
        c.weightx = 0.8;
        this.add(userInput, c);
        c.weightx = 0.2;
        this.add(submit, c);

        this.setSize(350, 500);
        this.userInput.setSize(170, 50);
        this.messageView.setSize(340, 400);
        this.submit.setSize(70, 50);

        messageView.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        messageView.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.doLayout();

        initialize();
    }

    private void setupMessageTable() {
        messageTable.setAutoCreateColumnsFromModel(true);
        messageTable.setFillsViewportHeight(true);
        messageTable.getColumnModel().getColumn(0).setResizable(false);
        messageTable.getColumnModel().getColumn(1).setResizable(false);

        messageTable.getColumnModel().getColumn(0).setWidth(80);
        messageTable.getColumnModel().getColumn(1).setWidth(260);
        messageTable.setVisible(true);
    }

    private void initialize() {
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
        System.out.println("Got a message enqueued: " + chatMessage.getMessage());
        tableModel.addNewMessage(chatMessage);
        return true;
    }

    private final class InputListener implements KeyListener {

        @Override
        public void keyPressed(KeyEvent ev) {
            if (ev.getKeyCode() == KeyEvent.VK_ENTER) {
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
