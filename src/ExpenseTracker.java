
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ExpenseTracker extends JApplet {
    private ArrayList<Expense> expenses;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField descField, amountField, categoryField;
    private JLabel totalLabel;

    @Override
    public void init() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    createGUI();
                }
            });
        } catch (Exception e) {
            System.err.println("createGUI didn't complete successfully");
        }
    }

    private void createGUI() {
        expenses = new ArrayList<>();
        setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add New Expense"));

        inputPanel.add(new JLabel("Description:"));
        descField = new JTextField();
        inputPanel.add(descField);

        inputPanel.add(new JLabel("Amount:"));
        amountField = new JTextField();
        inputPanel.add(amountField);

        inputPanel.add(new JLabel("Category:"));
        categoryField = new JTextField();
        inputPanel.add(categoryField);

        JButton addButton = new JButton("Add Expense");
        addButton.addActionListener(new AddExpenseListener());
        inputPanel.add(addButton);

        JButton clearButton = new JButton("Clear Inputs");
        clearButton.addActionListener(e -> clearInputs());
        inputPanel.add(clearButton);

        add(inputPanel, BorderLayout.NORTH);

        // Table Panel
        String[] columnNames = { "Description", "Amount", "Category" };
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Total Panel
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalLabel = new JLabel("Total Expenses: ₹0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalPanel.add(totalLabel);
        add(totalPanel, BorderLayout.SOUTH);
    }

    private void clearInputs() {
        descField.setText("");
        amountField.setText("");
        categoryField.setText("");
    }

    private void updateTotal() {
        double total = 0;
        for (Expense e : expenses) {
            total += e.getAmount();
        }
        totalLabel.setText(String.format("Total Expenses: ₹%.2f", total));
    }

    private class AddExpenseListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String desc = descField.getText().trim();
            String amountStr = amountField.getText().trim();
            String category = categoryField.getText().trim();

            if (desc.isEmpty() || amountStr.isEmpty() || category.isEmpty()) {
                JOptionPane.showMessageDialog(ExpenseTracker.this, "Please fill in all fields.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double amount = Double.parseDouble(amountStr);
                Expense expense = new Expense(desc, amount, category);
                expenses.add(expense);

                // Add to table
                tableModel
                        .addRow(new Object[] { expense.getDescription(), expense.getAmount(), expense.getCategory() });

                updateTotal();
                clearInputs();
                JOptionPane.showMessageDialog(ExpenseTracker.this, "Expense added successfully!", "Success",
                        JOptionPane.PLAIN_MESSAGE, new CheckIcon());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(ExpenseTracker.this, "Invalid amount format.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Custom Icon for Verified Tick
    private static class CheckIcon implements Icon {
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw Green Circle
            g2.setColor(new Color(34, 139, 34)); // Forest Green
            g2.fillOval(x, y, getIconWidth(), getIconHeight());

            // Draw White Checkmark
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(3));
            g2.drawLine(x + 10, y + 20, x + 18, y + 28);
            g2.drawLine(x + 18, y + 28, x + 30, y + 12);

            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return 40;
        }

        @Override
        public int getIconHeight() {
            return 40;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Expense Tracker");
        ExpenseTracker applet = new ExpenseTracker();
        applet.init();
        frame.add(applet);
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
