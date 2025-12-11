# PROBLEM STATEMENT
Managing daily expenses manually can be tedious and prone to errors. Individuals often lose track of their spending, making it difficult to budget effectively. The **Expense Tracker** application solves this problem by providing a user-friendly interface to record, view, and automatically calculate the total of daily expenses. It ensures data accuracy and offers an organized view of financial habits.

# 1. Class Diagram Description
The application is structured around two primary classes that follow a simplified Model-View-Controller (MVC) pattern:

1.  **`Expense` (Model)**:
    *   This class represents the data for a single expense entry.
    *   It encapsulates the details: `description` (what was bought), `amount` (cost), and `category` (type of expense).
    *   It provides accessor methods (getters) to retrieve these values and overrides `toString()` for text representation.

2.  **`ExpenseTracker` (View & Controller)**:
    *   This is the main class representing the application window (extending `JApplet` for legacy compatibility and containing a `main` method for standalone `JFrame` execution).
    *   It manages the **GUI components**: Text fields for input, a table (`JTable`) to display the list of expenses, and a label for the total amount.
    *   **Event Handling**: It includes two inner classes:
        *   `AddExpenseListener`: Handles the "Add Expense" button click, validates user input, updates the list, and refreshes the display.
        *   `CheckIcon`: A custom graphics class that draws a green checkmark icon for the success confirmation popup.

# 2. Class Diagram (Textual Representation)

```text
+-------------------+                   +-------------------+
|   ExpenseTracker  |                   |      Expense      |
+-------------------+                   +-------------------+
| - expenses: List  |<>-----------------| - description: Str|
| - table: JTable   |                   | - amount: double  |
| - total: JLabel   |                   | - category: String|
+-------------------+                   +-------------------+
| + init()          |                   | + getDescription()|
| + createGUI()     |                   | + getAmount()     |
| + main()          |                   | + getCategory()   |
+-------------------+                   +-------------------+
          |
          v
+-----------------------+
|   AddExpenseListener  |
+-----------------------+
| + actionPerformed()   |
+-----------------------+
```

# 3. Java Code

### Expense.java
```java
import java.io.Serializable;

public class Expense implements Serializable {
    private String description;
    private double amount;
    private String category;

    public Expense(String description, double amount, String category) {
        this.description = description;
        this.amount = amount;
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return description + " - ₹" + amount + " (" + category + ")";
    }
}
```

### ExpenseTracker.java
```java
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
```

# 4. Applicable Test Cases

| Case ID | Test Case Scenario | Input Data | Expected Outcome |
| :--- | :--- | :--- | :--- |
| **TC-01** | **Add Valid Expense** | Desc: "Coffee"<br>Amt: "50"<br>Cat: "Food" | Expense is added to the table. Total updates to include 50. Success popup appears. |
| **TC-02** | **Invalid Amount Format** | Desc: "Taxi"<br>Amt: "abc"<br>Cat: "Travel" | Error popup: "Invalid amount format." Expense is NOT added. |
| **TC-03** | **Empty Fields** | Desc: ""<br>Amt: "100"<br>Cat: "" | Error popup: "Please fill in all fields." |
| **TC-04** | **Clear Inputs** | User types data in fields, then clicks "Clear Inputs". | All input text fields are cleared to empty strings. |
| **TC-05** | **Calculation Logic** | Add "10", then add "20". | Total label displays "Total Expenses: ₹30.00". |

# 5. Result Description
When the application is executed, the user is presented with a clean GUI:

1.  **Input Form**: Located at the top, allowing entry of Description, Amount, and Category.
2.  **Expense Table**: Located in the center, showing the history of all entered expenses with columns for Description, Amount, and Category.
3.  **Total Indicator**: Located at the bottom right (`Total Expenses: ₹...`), which updates in real-time as expenses are added.
4.  **Confirmation**: Upon successfully adding an expense, a "Success" dialog box appears with a custom green checkmark icon, confirming the action to the user.

# Conclusion
The **Expense Tracker** project successfully demonstrates the creation of a functional Java desktop application using Swing. It meets the core requirement of helping users track expenses through an intuitive interface. The implementation highlights key programming concepts such as Object-Oriented Design, Event Driven Programming, and GUI construction in Java.
