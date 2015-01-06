package de.vogel612.testclient_javabot.client;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import com.gmail.inverseconduit.datatype.ChatMessage;

public class ChatTableModel extends AbstractTableModel implements TableModel {

    private final List<ChatMessage> messages = new ArrayList<>();

    @Override
    public String getColumnName(int index) {
        if (index == 0) { return "Username"; }
        return "Message";
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public int getRowCount() {
        return messages.size();
    }

    @Override
    public Class< ? > getColumnClass(int column) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public Object getValueAt(int row, int column) {
        //TODO: Validations??
        final ChatMessage message = messages.get(row);
        if (column == 0) { return message.getUsername(); }
        return message.getMessage();
    }

    public void addNewMessage(ChatMessage message) {
        messages.add(message);
        fireTableRowsInserted(messages.size() - 2, messages.size());
    }

}
