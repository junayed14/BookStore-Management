package bookStore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableCellRenderer;  // Import TableCellRenderer
import java.awt.*;
import java.util.List;

public class AllUserScreen extends JFrame {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private List<Users> users; // Store the user list

    public AllUserScreen() {
        setTitle("All Users"); // Window title
        setSize(600, 400); // Set window size
        setLocationRelativeTo(null); // Center the window
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setIconImage(new ImageIcon("favicon.jpeg").getImage());// Only close this window, not AdminScreen

        // Create table (Hide "Role" column, show "Change Role" column)
        String[] columnNames = {"Username", "Password", "Change Role", "Action"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2 || column == 3; // Only allow editing in "Change Role" and "Confirm" columns
            }
        };

        userTable = new JTable(tableModel);
        userTable.getColumnModel().getColumn(1).setMaxWidth(0);  // Hide "Password" column
        userTable.getColumnModel().getColumn(1).setMinWidth(0);
        userTable.getColumnModel().getColumn(1).setPreferredWidth(0); // "Password" column is hidden

        // Load users into table
        loadUsers();

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);

        // Set custom editor for the "Change Role" column (index 2)
        setRoleEditor();

        // Show window
        setVisible(true);
    }

    private void loadUsers() {
        users = FileHandler.readUsers(); // Read user list from file

        for (Users user : users) {
            tableModel.addRow(new Object[]{user.getUsername(), user.getPassword(), user.getRole(), "Confirm"});
        }
    }

    private void setRoleEditor() {
        String[] roles = {"admin", "customer"};
        JComboBox<String> roleDropdown = new JComboBox<>(roles);

        // Set the cell editor for the "Change Role" column (index 2)
        TableColumn roleColumn = userTable.getColumnModel().getColumn(2);
        roleColumn.setCellEditor(new DefaultCellEditor(roleDropdown));

        // Listen for role changes
        roleDropdown.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow != -1) {
                // Get the new role from the dropdown and store it temporarily
                String newRole = (String) roleDropdown.getSelectedItem();
                users.get(selectedRow).setRole(newRole);
            }
        });

        // Add a confirm button in the "Confirm" column (index 3)
        userTable.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        userTable.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JCheckBox(), this));
    }

    // Custom renderer for the "Confirm" button
    public class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setText("Change");
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Custom editor for the "Confirm" button
    public class ButtonEditor extends DefaultCellEditor {
        private String label;
        private AllUserScreen parentFrame;

        public ButtonEditor(JCheckBox checkBox, AllUserScreen parentFrame) {
            super(checkBox);
            this.parentFrame = parentFrame;
            setClickCountToStart(1); // Allow editor to start on first click
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            JButton button = new JButton(label);
            button.addActionListener(e -> {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow != -1) {
                    // Update the role and save the users list when "Confirm" is clicked
                    String newRole = users.get(selectedRow).getRole();
                    users.get(selectedRow).setRole(newRole);  // Update role
                    FileHandler.writeUsers(users); // Save updated users list
                    JOptionPane.showMessageDialog(parentFrame, "Role updated for " + users.get(selectedRow).getUsername());
                }
            });
            return button;
        }
    }
}
