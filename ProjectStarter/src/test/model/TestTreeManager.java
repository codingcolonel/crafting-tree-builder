package model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class TestTreeManager {
    private TreeManager testTreeManager1;
    private TreeManager testTreeManager2;

    private Item testItem1;
    private Item testItem2a;
    private Item testItem3a;
    private Item testItem3b;
    private Item testItem2b;

    private Node testNode1;
    private Node testNode2;
    private Node testNode3;

    private Node child2a;
    private Node child3a;
    private Node child3b;

    @BeforeEach
    public void runBefore() {
        testItem1 = new Item("Item1", Category.COMPONENT, Method.REFINE, 1, 100);
        testItem2a = new Item("Item2", Category.CONSUMABLE, Method.CRAFT, 5, 500);
        testItem3a = new Item("Item3", Category.COMPONENT, Method.REFINE, 7, 1000);
        testItem3b = new Item("Item4", Category.CONSUMABLE, Method.CRAFT, 10, 750);
        testItem2b = new Item("Item5", Category.CONSUMABLE, Method.CRAFT, 4, 1000);

        testNode1 = new Node(testItem1);
        testNode2 = new Node(testItem1);
        testNode3 = new Node(testItem2a);

        child2a = testNode1.addChild(testItem2a);
        child3a = child2a.addChild(testItem3a);
        child3b = child2a.addChild(testItem3b);

        testTreeManager1 = new TreeManager();
        testTreeManager2 = new TreeManager();
        testTreeManager2.setRoot(testNode1);
    }

    @Test
    public void constructorTest() {
        assertEquals(null, testTreeManager1.getRoot());
        assertEquals(null, testTreeManager1.getSelected());
    }

    @Test
    public void settersTest() {
        testTreeManager1.setRoot(testNode2);
        assertEquals(testNode2, testTreeManager1.getRoot());

        Node result = testTreeManager1.setRoot(testItem1);
        assertEquals(result, testTreeManager1.getRoot());

        testTreeManager1.setSelected(child2a);
        assertEquals(child2a, testTreeManager1.getSelected());

        List<Node> listNode = new ArrayList<Node>();
        testTreeManager1.setSavedTrees(listNode);
    }

    @Test
    public void getSavedTreesTest() {
        List<Node> result = testTreeManager1.getSavedTrees();
        assertEquals(new ArrayList<Node>(), result);
    }

    @Test
    public void getNodeAtIndexTest() {
        Node result = testTreeManager2.getNodeAtIndex(1);
        assertEquals(child2a, result);
    }

    @Test
    public void getTreeListTest() {
        List<Item> expected = new ArrayList<Item>();
        expected.add(testItem1);
        expected.add(testItem2a);
        expected.add(testItem3a);
        expected.add(testItem3b);

        List<Item> result = testTreeManager2.getTreeList();

        assertEquals(expected, result);
    }

    @Test
    public void getTreeListOneNodeTest() {
        List<Item> expected = new ArrayList<Item>();
        expected.add(testItem2a);

        testTreeManager1.setRoot(testNode3);
        List<Item> result = testTreeManager1.getTreeList();

        assertEquals(expected, result);
    }

    @Test
    public void baseComponentsListTest() {
        List<Item> expected = new ArrayList<Item>();
        expected.add(testItem3a);
        expected.add(testItem3b);

        List<Item> result = testTreeManager2.baseComponentsList();

        assertEquals(2, result.size());

        assertEquals(expected.get(0).getName(), result.get(0).getName());
        assertEquals(expected.get(0).getCategory(), result.get(0).getCategory());
        assertEquals(expected.get(0).getMethod(), result.get(0).getMethod());
        assertEquals(expected.get(0).getAmount(), result.get(0).getAmount());
        assertEquals(expected.get(0).getCost(), result.get(0).getCost());

        assertEquals(expected.get(1).getName(), result.get(1).getName());
        assertEquals(expected.get(1).getCategory(), result.get(1).getCategory());
        assertEquals(expected.get(1).getMethod(), result.get(1).getMethod());
        assertEquals(expected.get(1).getAmount(), result.get(1).getAmount());
        assertEquals(expected.get(1).getCost(), result.get(1).getCost());
    }

    @Test
    public void baseComponentsListOneNodeTest() {
        List<Item> expected = new ArrayList<Item>();
        expected.add(testItem2a);

        testTreeManager1.setRoot(testNode3);
        List<Item> result = testTreeManager1.baseComponentsList();

        assertEquals(1, result.size());

        assertEquals(expected.get(0).getName(), result.get(0).getName());
        assertEquals(expected.get(0).getCategory(), result.get(0).getCategory());
        assertEquals(expected.get(0).getMethod(), result.get(0).getMethod());
        assertEquals(expected.get(0).getAmount(), result.get(0).getAmount());
        assertEquals(expected.get(0).getCost(), result.get(0).getCost());
    }

    @Test
    public void baseComponentsListDuplicatesTest() {
        testNode1.addChild(testItem3a);

        List<Item> expected = new ArrayList<Item>();
        Item combinedItem = new Item(testItem3a);
        combinedItem.multiplyCost(2);
        combinedItem.multiplyAmount(2);

        expected.add(combinedItem);
        expected.add(testItem3b);

        List<Item> result = testTreeManager2.baseComponentsList();

        assertEquals(expected.get(0).getName(), result.get(0).getName());
        assertEquals(expected.get(0).getCategory(), result.get(0).getCategory());
        assertEquals(expected.get(0).getMethod(), result.get(0).getMethod());
        assertEquals(expected.get(0).getAmount(), result.get(0).getAmount());
        assertEquals(expected.get(0).getCost(), result.get(0).getCost());

        assertEquals(expected.get(1).getName(), result.get(1).getName());
        assertEquals(expected.get(1).getCategory(), result.get(1).getCategory());
        assertEquals(expected.get(1).getMethod(), result.get(1).getMethod());
        assertEquals(expected.get(1).getAmount(), result.get(1).getAmount());
        assertEquals(expected.get(1).getCost(), result.get(1).getCost());
    }

    @Test
    public void baseComponentsCostTest() {
        int result = testTreeManager2.baseComponentsCost();

        assertEquals(1750, result);
    }

    @Test
    public void baseComponentsCostOneNodeTest() {
        testTreeManager1.setRoot(testNode2);
        int result = testTreeManager1.baseComponentsCost();

        assertEquals(100, result);
    }

    @Test
    public void addChildAtSelectedNodeTest() {
        testTreeManager2.setSelected(child2a);
        Node result = testTreeManager2.addChildAtSelectedNode(testItem2b);
        assertEquals(testItem2b, result.getItemData());
        assertEquals(result, child2a.getChildren().get(2));
    }

    @Test
    public void removeSelectedNodeTest() {
        testTreeManager2.setSelected(child2a);

        testTreeManager2.removeSelectedNode();
        assertEquals(new ArrayList<Node>(), testNode1.getChildren());
    }

    @Test
    public void removeSelectedNodeNoParentTest() {
        testTreeManager2.setSelected(testNode1);

        testTreeManager2.removeSelectedNode();
        assertEquals(null, testTreeManager2.getRoot());
        assertEquals(null, testTreeManager2.getSelected());
    }
}
