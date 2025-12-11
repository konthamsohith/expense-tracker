# Expense Tracker - Project Documentation

## 1. Project Overview
The **Expense Tracker** is a Java-based desktop application designed to help users manage their daily expenses. It provides a graphical user interface (GUI) to add, view, and track expenses.

Originally designed as an Applet, this project has been refactored to run as a standalone Java Swing application, ensuring compatibility with modern Java environments without requiring browser plugins.

## 2. Features
- **Add Expense**: Users can input a description, amount, and category for each expense.
- **Expense List**: Displays all added expenses in a tabular format.
- **Real-time Total**: Automatically calculates and updates the total sum of all expenses.
- **Input Validation**: Ensures all fields are filled and the amount is a valid number before adding.
- **Clear Inputs**: Button to quickly reset the input fields.

## 3. Technical Architecture
The project follows a simplified **Model-View-Controller (MVC)** design pattern:

### 3.1. Model
**File**: `src/Expense.java`
- Represents a single expense entry.
- **Fields**:
  - `description` (String): Details of the expense.
  - `amount` (double): Cost of the expense.
  - `category` (String): Category (e.g., Food, Transport).
- **Methods**: Standard getters and a `toString()` method. Implements `Serializable`.

### 3.2. View & Controller
**File**: `src/ExpenseTracker.java`
- Acts as both the View (GUI) and Controller (Logic).
- **Inheritance**: Extends `JApplet` (legacy support) but includes a `main` method for standalone execution via `JFrame`.
- **Key Components**:
  - `JTextField`: For user input.
  - `JTable` & `DefaultTableModel`: For displaying the list of expenses.
  - `JLabel`: For displaying the total amount.
- **Inner Classes**:
  - `AddExpenseListener`: Handles the "Add Expense" button click event, validates input, updates the model, and refreshes the view.

## 4. Project Structure
```text
f:\Expense tracker\
├── src\
│   ├── Expense.java        # Data Model
│   └── ExpenseTracker.java # Main Application Logic & GUI
└── README.md               # This documentation
```

## 5. How to Build and Run

### Prerequisites
- **Java Development Kit (JDK)**: Ensure Java is installed and `javac`/`java` are in your system PATH.

### Compilation
Open a terminal (Command Prompt or PowerShell) in the project root (`f:\Expense tracker`) and run:

```bash
javac -d . src/Expense.java src/ExpenseTracker.java
```
*This compiles the source files and places the generated `.class` files in the current directory.*

### Execution
To run the application, execute:

```bash
java ExpenseTracker
```
*This will launch the Expense Tracker window.*

## 6. Usage Guide
1.  **Launch the Application** using the command above.
2.  **Enter Details**:
    -   **Description**: e.g., "Lunch"
    -   **Amount**: e.g., "15.50"
    -   **Category**: e.g., "Food"
3.  Click **Add Expense**. The item will appear in the table, and the "Total Expenses" at the bottom will update.
4.  Use **Clear Inputs** if you want to reset the fields without adding an expense.
