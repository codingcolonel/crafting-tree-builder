package model;

import java.util.ArrayList;
import java.util.List;

/**
 * TreeManager manages the active tree by adding/removing new nodes, summarizing
 * statistics on the active tree, and switching out the active root node as
 * needed
 */
public class TreeManager {
    private Node root;
    private Node selected;
    private List<Node> savedTrees;

    // EFFECTS: Creates a new TreeManager with no root or selected Nodes
    public TreeManager() {
        savedTrees = new ArrayList<Node>();
    }

    public Node getRoot() {
        return root;
    }

    public Node getSelected() {
        return selected;
    }

    public List<Node> getSavedTrees() {
        return savedTrees;
    }

    public Node getNodeAtIndex(int index) {
        return root.getListOfNodes().get(index);
    }

    public void setSavedTrees(List<Node> listOfNode) {
        savedTrees = listOfNode;
        EventLog.getInstance().logEvent(new Event("Set saved trees to a new list"));
    }

    public void addToSavedTrees(Node node) {
        savedTrees.add(node);
        EventLog.getInstance().logEvent(new Event("Added " + node.getItemData().getName() + " node to saved trees"));
    }

    // MODIFIES: this
    // EFFECTS: sets an existing node to root
    public void setRoot(Node node) {
        root = node;
        EventLog.getInstance().logEvent(new Event("Set " + node.getItemData().getName() + " node to root"));
    }

    // REQUIRES: amount > 0
    // MODIFIES: this
    // EFFECTS: sets a new node to root, returns new node
    public Node setRoot(Item item) {
        Node newNode = new Node(item);
        root = newNode;
        EventLog.getInstance().logEvent(new Event("Set new " + item.getName() + " node to root"));
        return newNode;
    }

    public void setSelected(Node selected) {
        this.selected = selected;
        EventLog.getInstance().logEvent(new Event("Set " + selected.getItemData().getName() + " node to selected"));
    }

    // REQUIRES: this.getRoot != null
    // EFFECTS: gets the whole tree in a list of Items ready to display
    public List<Item> getTreeList() {
        List<Node> listOfNodes = root.getListOfNodes();
        List<Item> listOfItems = new ArrayList<Item>();
        for (Node node : listOfNodes) {
            listOfItems.add(node.getItemData());
        }
        return listOfItems;
    }

    // REQUIRES: this.getRoot != null
    // EFFECTS: gets the list of all the base components in the tree with duplicates
    // combined together
    public List<Item> baseComponentsList() {
        List<Node> listOfNodes = root.getBaseComponents();
        List<Item> listOfItems = new ArrayList<Item>();

        for (Node node : listOfNodes) {
            addOrIncrementItem(node, listOfItems);
        }

        return listOfItems;
    }

    // MODIFIES: listOfItems
    // EFFECTS: checks list of items for given string, if two names match then that
    // item is incremented by the values of node
    // otherwise the node is add to listOfItems
    private static void addOrIncrementItem(Node node, List<Item> listOfItems) {
        Item nodeData = node.getItemData();
        for (Item item : listOfItems) {
            if (item.getName() == nodeData.getName()) {
                item.addAmount(nodeData.getAmount());
                item.addCost(nodeData.getCost());
                EventLog.getInstance().logEvent(new Event("Combined similar " + node.getItemData().getName()));
                return;
            }
        }
        Item clonedData = new Item(nodeData);
        listOfItems.add(clonedData);
        EventLog.getInstance().logEvent(new Event("Did not find similar items to combine"));
    }

    // REQUIRES: this.getRoot != null
    // EFFECTS: gets the cost of all the base components in the tree
    public int baseComponentsCost() {
        List<Node> listOfNodes = root.getBaseComponents();
        int sum = 0;
        for (Node node : listOfNodes) {
            sum += node.getItemData().getCost();
        }
        return sum;
    }

    // REQUIRES: selected != null
    // MODIFIES: this
    // EFFECT: Adds a new node at selected node in the tree
    public Node addChildAtSelectedNode(Item item) {
        Node newNode = selected.addChild(item);
        return newNode;
    }

    // REQUIRES: selected != null
    // MODIFIES: this.selected.getParent()
    // EFFECT: Removes selected node from the tree (remove from parent's children)
    public void removeSelectedNode() {
        if (selected.getParent() == null) {
            root = null;
            selected = null;
            EventLog.getInstance().logEvent(new Event("Deselected " + selected.getItemData().getName()));
        } else {
            selected.removeNode();
            selected = null;
            EventLog.getInstance()
                    .logEvent(new Event("Removed " + selected.getItemData().getName() + " from selected"));
        }   
    }
}
