package model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Node is a single node in a crafting tree (class X)
 * Node has it's item data, an amount of the item to craft one instance of
 * the root node (highest parent) it's parent (node above in the tree), and it's
 * children (nodes below in the tree)
 */
public class Node {
    Item itemData;
    Node parent;
    List<Node> children;

    // EFFECTS: Creates new node with item data, no parent, and an empty
    // list of children
    public Node(Item data) {
        this.itemData = data;
        this.children = new ArrayList<Node>();
    }

    public Item getItemData() {
        return itemData;
    }

    public Node getParent() {
        return parent;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setItemData(Item data) {
        this.itemData = data;
    }

    // EFFECTS: Gets the amount of indenting this node should have based on the
    // distance to the top of the tree
    public String getIndent() {
        Node activeNode = this;
        String indent = "";
        while (activeNode.getParent() != null) {
            indent += "\t";
            activeNode = activeNode.getParent();
        }
        return indent;
    }

    // MODIFIES: this
    // EFFECTS: Adds new node to list of children and sets the parent of the new
    // node to itself
    // Returns the reference to the new node
    public Node addChild(Item item) {
        Node newNode = new Node(item);
        newNode.setParent(this);
        children.add(newNode);
        EventLog.getInstance()
                .logEvent(new Event("Added child " + item.getName() + " to parent " + itemData.getName()));
        return newNode;
    }

    // REQUIRES: this.getParent != null
    // MODIFIES: this, this.getParent()
    // EFFECTS: Removes node from it's parent's children, Node is not meant to be
    // accessed again after removed
    public void removeNode() {
        parent.getChildren().remove(this);
        EventLog.getInstance().logEvent(
                new Event("Removed " + itemData.getName() + " from parent " + parent.getItemData().getName()));
    }

    // MODIFIES: this
    // EFFECTS: Sets the parent to given node
    // NOTE: Only public for testing purposes, should not be called outside Node
    public void setParent(Node node) {
        this.parent = node;
        EventLog.getInstance().logEvent(new Event("Set parent to " + node.getItemData().getName()));
    }

    // EFFECTS: Mutually recursive function that traverses that tree and returns all
    // nodes that pass the Predicate function
    public List<Node> traverseTreeNode(Node node, Predicate<Node> condition) {
        List<Node> listOfValues = new ArrayList<Node>();

        if (condition.test(node)) {
            listOfValues.add(this);
        }
        listOfValues.addAll(node.traverseTreeList(this.children, condition));
        return listOfValues;
    }

    public List<Node> traverseTreeList(List<Node> children, Predicate<Node> condition) {
        List<Node> listOfValues = new ArrayList<Node>();
        if (children.size() == 0) {
            return new ArrayList<Node>();
        } else {
            for (Node node : children) {
                listOfValues.addAll(node.traverseTreeNode(node, condition));
            }
            return listOfValues;
        }
    }

    // EFFECTS: Returns a list of a nodes in the tree that belong the given category
    public List<Node> allNodesWithCategory(Category category) {
        Predicate<Node> condition = node -> node.itemData.getCategory() == category;
        return traverseTreeNode(this, condition);
    }

    // EFFECTS: Returns a node in the tree with the same name, returns null if none
    // was found
    public List<Node> allNodesWithName(String name) {
        Predicate<Node> condition = node -> node.itemData.getName() == name;
        return traverseTreeNode(this, condition);
    }

    // EFFECTS: Make a list of all base component nodes for current node
    public List<Node> getBaseComponents() {
        Predicate<Node> condition = node -> node.getChildren().size() == 0;
        return traverseTreeNode(this, condition);
    }

    // EFFECTS: Make a list of all nodes in the tree
    public List<Node> getListOfNodes() {
        Predicate<Node> condition = node -> true;
        return traverseTreeNode(this, condition);
    }

    @Override
    // EFFECTS: Overides the toString function to display proper tree String value
    public String toString() {
        return Integer.toString(getItemData().getAmount()) + " " + getItemData().getName();
    }
}
