package ui;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import model.Category;
import model.Item;
import model.Method;
import model.Node;
import model.TreeManager;
import persistence.JsonReader;
import persistence.JsonWriter;

/**
 * ConsoleApp is a basic console UI that allows the creation and management
 * of saved or new crafting trees
 */
public class ConsoleApp {
    private TreeManager treeManager;
    private JsonReader jsonReader;
    private JsonWriter jsonWriter;

    public ConsoleApp() {
        treeManager = new TreeManager();
        jsonReader = new JsonReader("data/savedTreesData.json");
        jsonWriter = new JsonWriter("data/savedTreesData.json");

        try {
            treeManager.setSavedTrees(jsonReader.read());
        } catch (IOException e) {
            System.err.println(e);
        }

        menu();
    }

    private void menu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                displayMainMenu();
                int choice = scanner.nextInt();
                System.out.println("-----------------------------------------------------------");

                switch (choice) {
                    case 1:
                        // Select a tree case
                        selectTree(scanner);
                        break;
                    case 2:
                        // New tree case
                        createTree(scanner);
                        break;
                    case 3:
                        // Delete tree case
                        deleteTree(scanner);
                        break;
                    case 4:
                        // Quit case
                        System.out.println("Goodbye!");
                        scanner.close();
                        return;
                    default:
                        // The user input an unexpected choice.
                        System.out.println("Invalid Option");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Invalid Selection");
                scanner.nextLine();
            }
        }
    }

    private void displayMainMenu() {
        System.out.println("-----------------------------------------------------------");
        System.out.println("Main Menu");
        System.out.println("1. View Saved Trees");
        System.out.println("2. Create New Tree");
        System.out.println("3. Delete Saved Tree");
        System.out.println("4. Quit");
        System.out.println("-----------------------------------------------------------");
        System.out.println("Choose an Option Below:");
    }

    private void selectTree(Scanner scanner) {
        List<Node> savedTrees = treeManager.getSavedTrees();
        if (savedTrees.size() == 0) {
            System.out.println("No Saved Trees to Display");
            return;
        }

        while (true) {
            try {
                displaySelectMenu(savedTrees);
                int choice = scanner.nextInt();
                Node selectedTree = treeManager.getSavedTrees().get(choice - 1);

                openTree(selectedTree, scanner);

                return;
            } catch (Exception e) {
                System.out.println("Invalid Selection");
                scanner.nextLine();
            }
        }
    }

    private void displaySelectMenu(List<Node> savedTrees) {
        int counter = 1;

        System.out.println("Choose a tree to open");

        for (Node node : savedTrees) {
            System.out.println(String.valueOf(counter) + ". " + node.getItemData().getName());
            counter++;
        }
    }

    private void createTree(Scanner scanner) {
        Node newCreatedNode = newNode(scanner);
        System.out.println("New Tree Created");
        openTree(newCreatedNode, scanner);
    }

    private Node newNode(Scanner scanner) {
        String name = "";
        Category category = Category.COMPONENT;
        Method method = Method.COOK;
        int amount = 3;
        int cost = 100;

        while (true) {
            try {
                scanner.nextLine();
                System.out.println("Enter a Name");
                name = scanner.nextLine();
                break;
            } catch (Exception e) {
                System.out.println("Invalid Name");
                scanner.nextLine();
            }
        }

        while (true) {
            try {
                System.out.println("Enter a Category (COMPONENT, TECHNOLOGY, CONSUMABLE)");
                category = Category.valueOf(scanner.nextLine().toUpperCase());
                break;
            } catch (Exception e) {
                System.out.println("Invalid Category");
                scanner.nextLine();
            }
        }
        while (true) {
            try {
                System.out.println("Enter a Method (CRAFT, REFINE, COOK)");
                method = Method.valueOf(scanner.nextLine().toUpperCase());
                break;
            } catch (Exception e) {
                System.out.println("Invalid Method");
                scanner.nextLine();
            }
        }
        while (true) {
            try {
                System.out.println("Enter an Amount");
                amount = Integer.parseInt(scanner.nextLine());
                break;
            } catch (Exception e) {
                System.out.println("Invalid Amount");
                scanner.nextLine();
            }
        }
        while (true) {
            try {
                System.out.println("Enter a Cost");
                cost = Integer.parseInt(scanner.nextLine());
                break;
            } catch (Exception e) {
                System.out.println("Invalid Cost");
                scanner.nextLine();
            }

        }
        Item newItem = new Item(name, category, method, amount, cost);
        return new Node(newItem);
    }

    private void deleteTree(Scanner scanner) {
        List<Node> savedTrees = treeManager.getSavedTrees();
        if (savedTrees.size() == 0) {
            System.out.println("No Saved Trees to Delete");
            return;
        }

        while (true) {
            try {
                displayDeleteMenu(savedTrees);
                int choice = scanner.nextInt();

                treeManager.getSavedTrees().remove(choice - 1);
                saveTrees();

                return;
            } catch (Exception e) {
                System.out.println("Invalid Selection");
                scanner.nextLine();
            }
        }
    }

    private void displayDeleteMenu(List<Node> savedTrees) {
        int counter = 1;

        System.out.println("Choose a tree to delete");

        for (Node node : savedTrees) {
            System.out.println(String.valueOf(counter) + ". " + node.getItemData().getName());
            counter++;
        }

        System.out.println("Enter Selection:");
    }

    private void openTree(Node node, Scanner scanner) {
        treeManager.setRoot(node);
        displayTree(scanner);
    }

    private void displayTree(Scanner scanner) {
        List<Item> selectedTree;

        while (true) {
            try {
                selectedTree = treeManager.getTreeList();
                displayTreeMenu(selectedTree);
                int choice = scanner.nextInt();
                System.out.println("-----------------------------------------------------------");

                switch (choice) {
                    case 1:
                        // Select Node
                        selectNode(scanner);
                        break;
                    case 2:
                        // Add sub-branch
                        addNode(scanner);
                        break;
                    case 3:
                        // Remove node
                        removeNode(scanner);
                        break;
                    case 4:
                        // All base components
                        displayBaseComponents(scanner);
                        break;
                    case 5:
                        // Base component cost
                        displayBaseComponentsCost(scanner);
                        break;
                    case 6:
                        // Save current tree
                        List<Node> savedTrees = treeManager.getSavedTrees();
                        if (savedTrees.size() == 0
                                || !(savedTrees.get(savedTrees.size() - 1) == treeManager.getRoot())) {
                            treeManager.addToSavedTrees(treeManager.getRoot());
                        }
                        saveTrees();
                        break;
                    case 7:
                        // Quit case
                        Boolean confirmExit = areYouSure(scanner);
                        if (confirmExit) {
                            return;
                        }
                        break;
                    default:
                        // The user input an unexpected choice.
                        System.out.println("Invalid Option");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Invalid Selection");
                scanner.nextLine();
            }
        }
    }

    private void displayTreeMenu(List<Item> selectedTree) {
        System.out.println("-----------------------------------------------------------");
        System.out.println("Crafting Tree for " + treeManager.getRoot().getItemData().getName());

        printItems(selectedTree, true);

        System.out.println("-----------------------------------------------------------");
        System.out.println("Tree Menu");
        System.out.println("1. Select a Node");
        System.out.println("2. Add a Sub-Branch to Selected Node");
        System.out.println("3. Remove Selected Node");
        System.out.println("4. Display All the Needed Base Components");
        System.out.println("5. Display the Cost of All the Base Components");
        System.out.println("6. Save Progress");
        System.out.println("7. Quit to Main Menu");

        if (treeManager.getSelected() == null) {
            System.out.println("\nSelected Node: None");
        } else {
            System.out.println("\nSelected Node: " + treeManager.getSelected().getItemData().getName());
        }

        System.out.println("-----------------------------------------------------------");
        System.out.println("Choose an Option Below:");
    }

    private void printItems(List<Item> listOfItems, boolean shouldIndent) {
        int counter = 1;
        String indentation = "";

        for (Item item : listOfItems) {
            if (shouldIndent) {
                indentation = treeManager.getNodeAtIndex(counter - 1).getIndent();
            }
            System.out.println(indentation + String.valueOf(counter) + ". " + item.getName() + " --  Category: "
                    + item.getCategory()
                    + " | Method: " + item.getMethod() + " | Amount: " + item.getAmount() + " | Cost: " + item.getCost()
                    + "\n");
            counter++;
        }
    }

    private void selectNode(Scanner scanner) {
        int num;

        while (true) {
            try {
                System.out.println("Choose the number of a node to select it");
                num = scanner.nextInt();

                Node selectedNode = treeManager.getNodeAtIndex(num - 1);
                treeManager.setSelected(selectedNode);
                System.out.println("Selected " + selectedNode.getItemData().getName());
                break;
            } catch (Exception e) {
                System.out.println("Invalid Selection");
                scanner.nextLine();
            }
        }
    }

    private void addNode(Scanner scanner) {
        if (treeManager.getSelected() == null) {
            System.out.println("No Node Selected!");
            return;
        }

        treeManager.addChildAtSelectedNode(newNode(scanner).getItemData());
    }

    private void removeNode(Scanner scanner) {
        if (treeManager.getSelected() == null) {
            System.out.println("No Node Selected!");
            return;
        }
        treeManager.removeSelectedNode();
    }

    private void displayBaseComponents(Scanner scanner) {
        List<Item> baseComponentsList = treeManager.baseComponentsList();
        System.out.println("All Base Components:");

        printItems(baseComponentsList, false);
    }

    private void displayBaseComponentsCost(Scanner scanner) {
        System.out.println(
                "Total Cost of Base Components: " + String.valueOf(treeManager.baseComponentsCost()) + " units");
    }

    private Boolean areYouSure(Scanner scanner) {
        while (true) {
            System.out.println("Are you sure to want to return to the menu? All unsaved progress will be lost");
            System.out.println("1. Yes");
            System.out.println("2. No");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Back to Main Menu...");
                    return true;
                case 2:
                    System.out.println("Going back...");
                    return false;
                default:
                    break;
            }
        }

    }

    private void saveTrees() {
        try {
            jsonWriter.open();
            jsonWriter.write(treeManager);
            jsonWriter.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
