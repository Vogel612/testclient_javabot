package de.vogel612.testclient_javabot.client.table;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

import com.gmail.inverseconduit.datatype.ChatMessage;

public class ChatCellRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        final ChatMessage message = (ChatMessage) value;
        JPanel usernameContainer = new JPanel();
        usernameContainer.setMinimumSize(new Dimension(40, 150));
        usernameContainer.setSize(new Dimension(40, 150));

        // don't get to negative indices!!
        if (row == 0 || ! ((ChatMessage) table.getValueAt(row - 1, column)).getUsername().equals(message.getUsername())) {
            usernameContainer.add(new JLabel(message.getUsername()));
        }

        JTextArea messageDisplay = new JTextArea(message.getMessage());
        messageDisplay.setEditable(false);
        messageDisplay.setLineWrap(true);
        messageDisplay.doLayout();
        JSplitPane cell = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false, usernameContainer, messageDisplay);
        cell.setMinimumSize(new Dimension(messageDisplay.getWidth() + usernameContainer.getWidth(), messageDisplay.getHeight()));
        cell.setDividerSize(1);
        cell.doLayout();
        //FIXME height calculation needs improvement
        table.setRowHeight(row, 90);
        return cell;
    }
}
