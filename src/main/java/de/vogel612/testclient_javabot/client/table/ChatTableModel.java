package de.vogel612.testclient_javabot.client.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import com.gmail.inverseconduit.datatype.ChatMessage;

public class ChatTableModel extends AbstractTableModel implements TableModel {

    private final List<ChatMessage> messages = new ArrayList<>();

    @Override
    public String getColumnName(int index) {
        return "Messages";
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public int getRowCount() {
        return messages.size();
    }

    @Override
    public Class< ? > getColumnClass(int column) {
        return ChatMessage.class;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public Object getValueAt(int row, int column) {
        return messages.get(row);
    }

    public void addNewMessage(ChatMessage message) {
        messages.add(message);
        fireTableRowsInserted(messages.size() - 2, messages.size());
    }

}
