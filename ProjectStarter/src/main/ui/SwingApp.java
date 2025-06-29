package ui;

import javax.swing.*;
import javax.swing.tree.*;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SwingApp {
    JFrame frame;
    JPanel treeListPanel;
    JPanel utilityPanel;
    JPanel treeViewerPanel;

    private TreeManager treeManager;
    private JsonReader jsonReader;
    private JsonWriter jsonWriter;

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    final int width = screenSize.width;
    final int height = screenSize.height;

    // EFFECTS: initializesclass variables and sets up frame
    public SwingApp() {
        // Load model
        treeManager = new TreeManager();
        jsonReader = new JsonReader("data/savedTreesData.json");
        jsonWriter = new JsonWriter("data/savedTreesData.json");

        // Create a new frame
        frame = new JFrame("No Man's Sky Crafting Tree Application");
        frame.setSize(width, height);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.setLayout(null);
        frame.setVisible(true);

        loadDataPopUp();

        loadPanels();
        updateTreeListPanel();
        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        frame.addWindowListener(new SwingWindowListener());
    }

    // EFFECTS: sets up the load data pop-up
    private void loadDataPopUp() {
        String[] options = { "Yes", "No" };
        int choice = JOptionPane.showOptionDialog(
                frame,
                "Would you like to load the data",
                "Welcome!",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                null);

        // Handle user choice
        if (choice == 0) {
            // yes
            try {
                treeManager.setSavedTrees(jsonReader.read());
            } catch (IOException e) {
                System.err.println(e);
            }
        } else if (choice == 1) {
            // no
            treeManager.setSavedTrees(new ArrayList<Node>());
        }
        frame.setVisible(false);
    }

    // EFFECTS: loads the main panels
    private void loadPanels() {
        // Panel for list of trees
        treeListPanel = new JPanel();
        treeListPanel.setBackground(Color.GRAY);
        treeListPanel.setBounds(0, 0, 6 * width / 16, height / 2);

        // Panel for utility
        utilityPanel = new JPanel();
        utilityPanel.setBackground(Color.BLACK);
        utilityPanel.setBounds(0, height / 2, 6 * width / 16, height / 2);

        // Panel for tree viewer
        treeViewerPanel = new JPanel();
        treeViewerPanel.setBackground(Color.WHITE);
        treeViewerPanel.setBounds(6 * width / 16, 0, 10 * width / 16, height);

        frame.add(treeListPanel);
        frame.add(utilityPanel);
        frame.add(treeViewerPanel);
    }

    // EFFECTS: resets and loads the tree list panel
    private void updateTreeListPanel() {
        treeListPanel.removeAll();

        JLabel label = new JLabel("Trees (double-click to select)", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        treeListPanel.add(label);

        List<String> listOfNames = new ArrayList<>();
        for (Node node : treeManager.getSavedTrees()) {
            listOfNames.add(node.getItemData().getName());
        }

        JList<String> list = new JList<>(listOfNames.toArray(new String[0]));
        listSetup(list);

        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(6 * width / 16, 7 * height / 17));
        treeListPanel.add(scrollPane);

        JPanel buttonPanel = new JPanel();

        setupButtonPanel(buttonPanel);
        treeListPanel.add(buttonPanel);

        frame.revalidate();
        frame.repaint();
    }

    // EFFECTS: sets up buttons for tree list
    private void setupButtonPanel(JPanel buttonPanel) {
        JButton addTreeButton = new JButton("Add Tree");
        addTreeListener(addTreeButton);

        JButton deleteTreeButton = new JButton("Delete Tree");
        deleteTreeListener(deleteTreeButton);

        JButton saveProgressButton = new JButton("Save Progress");
        saveProgressListener(saveProgressButton);

        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(addTreeButton);
        buttonPanel.add(deleteTreeButton);
        buttonPanel.add(saveProgressButton);
        buttonPanel.setBackground(Color.GRAY);
    }

    // EFFECTS: listener for save progress
    private void saveProgressListener(JButton saveProgressButton) {
        saveProgressButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveProgress();
            }
        });
    }

    // EFFECTS: listener for delete tree
    private void deleteTreeListener(JButton deleteTreeButton) {
        deleteTreeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (treeManager.getRoot() != null) {
                    removeListItem(treeManager.getRoot());
                }
            }
        });
    }

    // EFFECTS: listener for add tree
    private void addTreeListener(JButton addTreeButton) {
        addTreeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayItemCreationInUtility(item -> {
                    Node newNode = new Node(item);
                    treeManager.addToSavedTrees(newNode);
                    updateTreeListPanel();
                    treeManager.setSelected(newNode);
                    displaySelectedInUtility();
                    treeManager.setRoot(newNode);
                    updateTreeViewerPanel();
                });
            }
        });
    }

    // EFFECTS: sets up the Jlist for list panel
    private void listSetup(JList<String> list) {
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFixedCellHeight(30);
        list.setFont(new Font("Arial", Font.PLAIN, 16));

        // Add click listener
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = list.locationToIndex(e.getPoint());

                    treeManager.setRoot(treeManager.getSavedTrees().get(index));
                    updateTreeViewerPanel();
                    utilityPanel.removeAll();
                    treeManager.setSelected(treeManager.getRoot());
                    displaySelectedInUtility();
                }
            }
        });
    }

    // EFFECTS: saves progress and displays pop-up upon success
    private void saveProgress() {
        try {
            // Save progress.
            jsonWriter.open();
            jsonWriter.write(treeManager);
            jsonWriter.close();

            // Save sucessful pop-up
            JOptionPane.showOptionDialog(
                    frame,
                    "All you data was saved sucessfully",
                    "Progress Saved",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    null,
                    null);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    // EFFECTS: resets and displays the tree viewer
    private void updateTreeViewerPanel() {
        treeViewerPanel.removeAll();

        DefaultMutableTreeNode mutableTree = createMutableTree(treeManager.getRoot());

        JTree tree = new JTree(mutableTree);

        // Expand all nodes by default
        tree.setRootVisible(true);
        tree.setShowsRootHandles(true);
        expandNodes(tree);

        // Remove folder/file icons, maybe add custom icons later?
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setFont(new Font("Arial", Font.BOLD, 30));
        renderer.setClosedIcon(null);
        renderer.setOpenIcon(null);
        renderer.setLeafIcon(null);
        tree.setCellRenderer(renderer);

        // Check if node was clicked
        treeListener(tree);

        // Wrap list in a scroll pane and add it to the treeListPanel
        JScrollPane scrollPane = new JScrollPane(tree);
        scrollPane.setPreferredSize(new Dimension(10 * width / 16, height));
        treeViewerPanel.add(scrollPane);
        frame.revalidate();
        frame.repaint();
    }

    // EFFECTS: listener for the tree elements
    private void treeListener(JTree tree) {
        tree.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                TreePath path = tree.getPathForLocation(e.getX(), e.getY());

                if (path == null) {
                    tree.clearSelection();
                } else {
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree
                            .getLastSelectedPathComponent();
                    Node theNode = (Node) selectedNode.getUserObject();
                    treeManager.setSelected(theNode);
                    displaySelectedInUtility();
                }
            }
        });
    }

    // EFFECTS: creates a mutable tree object for given node and children
    public DefaultMutableTreeNode createMutableTree(Node node) {
        DefaultMutableTreeNode mutableTreeNode = new DefaultMutableTreeNode(node);
        for (Node child : node.getChildren()) {
            DefaultMutableTreeNode mutableChildTree = createMutableTree(child);
            mutableTreeNode.add(mutableChildTree);
        }
        return mutableTreeNode;
    }

    // MODIFIES: tree
    // EFFECTS: expands all rows in the tree so it's automatically fully displayed
    private static void expandNodes(JTree tree) {
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
    }

    // EFFECTS: displays selected node in tree manager to utility panel
    private void displaySelectedInUtility() {
        Node node = treeManager.getSelected();

        utilityPanel.removeAll();
        utilityPanel.setLayout(new BoxLayout(utilityPanel, BoxLayout.Y_AXIS));

        Item item = node.getItemData();

        // Info labels
        JLabel nameLabel = new JLabel(String.valueOf(item.getName()), SwingConstants.CENTER);
        JLabel amountLabel = new JLabel("\nAmount:" + String.valueOf(item.getAmount()), SwingConstants.CENTER);
        JLabel costLabel = new JLabel("\nCost:" + String.valueOf(item.getCost()), SwingConstants.CENTER);
        JLabel categoryLabel = new JLabel("\nCategory:" + String.valueOf(item.getCategory()), SwingConstants.CENTER);
        JLabel methodLabel = new JLabel("\nMethod:" + String.valueOf(item.getMethod()), SwingConstants.CENTER);

        // Text color
        setupLabels(nameLabel, amountLabel, costLabel, categoryLabel, methodLabel);

        // Add labels to panel (with spacing)
        addLabelsToPanel(nameLabel, amountLabel, costLabel, categoryLabel, methodLabel);

        // Add buttons
        JButton addChildButton = new JButton("Add a Child");
        addChildListener(addChildButton);

        JButton deleteNodeButton = new JButton("Delete Node");
        deleteButtonListener(node, deleteNodeButton);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(addChildButton);
        buttonPanel.add(deleteNodeButton);

        utilityPanel.add(buttonPanel);

        frame.revalidate();
        frame.repaint();
    }

    // EFFECTS: adds labels to the utility panel with spacing
    private void addLabelsToPanel(JLabel nameLabel, JLabel amountLabel, JLabel costLabel, JLabel categoryLabel,
            JLabel methodLabel) {
        utilityPanel.add(Box.createVerticalStrut(30));
        utilityPanel.add(nameLabel);
        utilityPanel.add(Box.createVerticalStrut(15));
        utilityPanel.add(amountLabel);
        utilityPanel.add(Box.createVerticalStrut(15));
        utilityPanel.add(costLabel);
        utilityPanel.add(Box.createVerticalStrut(15));
        utilityPanel.add(categoryLabel);
        utilityPanel.add(Box.createVerticalStrut(15));
        utilityPanel.add(methodLabel);
        utilityPanel.add(Box.createVerticalStrut(15));
    }

    // EFFECTS: sets up text color and fonts for the labels in the utility panel
    private void setupLabels(JLabel nameLabel, JLabel amountLabel, JLabel costLabel, JLabel categoryLabel,
            JLabel methodLabel) {
        // Text color
        nameLabel.setForeground(Color.WHITE);
        amountLabel.setForeground(Color.WHITE);
        costLabel.setForeground(Color.WHITE);
        categoryLabel.setForeground(Color.WHITE);
        methodLabel.setForeground(Color.WHITE);

        // Text size
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 64));
        amountLabel.setFont(new Font("Arial", Font.PLAIN, 40));
        costLabel.setFont(new Font("Arial", Font.PLAIN, 40));
        categoryLabel.setFont(new Font("Arial", Font.PLAIN, 40));
        methodLabel.setFont(new Font("Arial", Font.PLAIN, 40));
    }

    // EFFECTS: listener for delete button
    private void deleteButtonListener(Node node, JButton deleteNodeButton) {
        deleteNodeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                utilityPanel.removeAll();
                if (node.getParent() == null) {
                    removeListItem(node);
                } else {
                    node.removeNode();
                    updateTreeViewerPanel();
                }
            }
        });
    }

    // EFFECTS: listener for add child
    private void addChildListener(JButton addChildButton) {
        addChildButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayItemCreationInUtility(item -> {
                    treeManager.getSelected().addChild(item);
                    treeManager.setSelected(treeManager.getSelected().getChildren()
                            .get(treeManager.getSelected().getChildren().size() - 1));
                    displaySelectedInUtility();
                    updateTreeViewerPanel();
                });
            }
        });
    }

    // EFFECTS: removes node from saved trees and updates panels
    private void removeListItem(Node node) {
        treeManager.getSavedTrees().remove(node);
        treeViewerPanel.removeAll();
        utilityPanel.removeAll();
        updateTreeListPanel();
    }

    // EFFECTS: sets up the item creation process in utility panel
    private void displayItemCreationInUtility(Consumer<Item> callback) {
        utilityPanel.removeAll();
        utilityPanel.setLayout(new BoxLayout(utilityPanel, BoxLayout.Y_AXIS));

        JPanel namePanel = new JPanel();
        JPanel amountPanel = new JPanel();
        JPanel costPanel = new JPanel();
        JPanel categoryPanel = new JPanel();
        JPanel methodPanel = new JPanel();

        JTextField nameField = setupNameField(namePanel);

        JTextField amountField = setupAmountField(amountPanel);

        JTextField costField = setupCostField(costPanel);

        JTextField categoryField = setupCategoryField(categoryPanel);

        JTextField methodField = setupMethodField(methodPanel);

        JButton submitButton = new JButton("Submit");

        utilityPanel.add(namePanel);
        utilityPanel.add(amountPanel);
        utilityPanel.add(costPanel);
        utilityPanel.add(categoryPanel);
        utilityPanel.add(methodPanel);

        utilityPanel.add(submitButton);

        frame.revalidate();
        frame.repaint();

        submitButtonListener(callback, nameField, amountField, costField, categoryField, methodField, submitButton);
    }

    // EFFECTS: sets up the method panel and textfield
    private JTextField setupMethodField(JPanel methodPanel) {
        methodPanel.setBackground(Color.BLACK);
        JLabel methodText = new JLabel("Enter a method (craft, refine, cook): ");
        methodText.setForeground(Color.WHITE);
        JTextField methodField = new JTextField(20);
        methodPanel.add(methodText);
        methodPanel.add(methodField);
        return methodField;
    }

    // EFFECTS: sets up the category panel and textfield
    private JTextField setupCategoryField(JPanel categoryPanel) {
        categoryPanel.setBackground(Color.BLACK);
        JLabel categoryText = new JLabel("Enter a category (component, technology, consumable): ");
        categoryText.setForeground(Color.WHITE);
        JTextField categoryField = new JTextField(20);
        categoryPanel.add(categoryText);
        categoryPanel.add(categoryField);
        return categoryField;
    }

    // EFFECTS: sets up the cost panel and textfield
    private JTextField setupCostField(JPanel costPanel) {
        costPanel.setBackground(Color.BLACK);
        JLabel costText = new JLabel("Enter a cost: ");
        costText.setForeground(Color.WHITE);
        JTextField costField = new JTextField(20);
        costPanel.add(costText);
        costPanel.add(costField);
        return costField;
    }

    // EFFECTS: sets up the amount panel and textfield
    private JTextField setupAmountField(JPanel amountPanel) {
        amountPanel.setBackground(Color.BLACK);
        JLabel amountText = new JLabel("Enter a amount: ");
        amountText.setForeground(Color.WHITE);
        JTextField amountField = new JTextField(20);
        amountPanel.add(amountText);
        amountPanel.add(amountField);
        return amountField;
    }

    // EFFECTS: sets up the name panel and textfield
    private JTextField setupNameField(JPanel namePanel) {
        namePanel.setBackground(Color.BLACK);
        JLabel nameText = new JLabel("Enter a name: ");
        nameText.setForeground(Color.WHITE);
        JTextField nameField = new JTextField(20);
        namePanel.add(nameText);
        namePanel.add(nameField);
        return nameField;
    }

    // EFFECTS: listener for submit button that sets up the method panel and
    // textfield
    private void submitButtonListener(Consumer<Item> callback, JTextField nameField, JTextField amountField,
            JTextField costField, JTextField categoryField, JTextField methodField, JButton submitButton) {
        submitButton.addActionListener(e -> {
            String nameInput;
            int amountInput;
            int costInput;
            Category categoryInput;
            Method methodInput;

            nameInput = nameField.getText();
            if (nameInput.isEmpty()) {
                invalidFieldPopUp("Invalid Name");
                return;
            }

            try {
                amountInput = Integer.valueOf(amountField.getText());
            } catch (Exception ex) {
                invalidFieldPopUp("Invalid Amount");
                return;
            }

            try {
                costInput = Integer.valueOf(costField.getText());
            } catch (Exception ex) {
                invalidFieldPopUp("Invalid Cost");
                return;
            }

            try {
                categoryInput = Category.valueOf(categoryField.getText().toUpperCase());
            } catch (Exception ex) {
                invalidFieldPopUp("Invalid Category");
                return;
            }

            try {
                methodInput = Method.valueOf(methodField.getText().toUpperCase());
            } catch (Exception ex) {
                invalidFieldPopUp("Invalid Method");
                return;
            }

            Item newItem = new Item(nameInput, categoryInput, methodInput, amountInput, costInput);
            callback.accept(newItem);
        });
    }

    // EFFECTS: displays pop-up for invalid field
    private void invalidFieldPopUp(String message) {
        JOptionPane.showOptionDialog(
                frame,
                message,
                "Error",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                null,
                null);
    }
}
