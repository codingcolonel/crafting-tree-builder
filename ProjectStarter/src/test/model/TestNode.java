package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class TestNode {
    private Item testItem1;
    private Item testItem2a;
    private Item testItem3a;
    private Item testItem3b;

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

        testNode1 = new Node(testItem1);
        testNode2 = new Node(testItem1);
        testNode3 = new Node(testItem2a);

        
        child2a = testNode1.addChild(testItem2a);
        child3a = child2a.addChild(testItem3a);
        child3b = child2a.addChild(testItem3b);
    }

    @Test
    public void constructorTest() {
        assertEquals(testItem1, testNode2.getItemData());
        assertEquals(null, testNode2.getParent());
        assertEquals(0, testNode2.getChildren().size());
    }

    @Test
    public void settersTest() {
        testNode2.setItemData(testItem2a);
        assertEquals(testItem2a, testNode2.getItemData());
        testNode2.setParent(testNode3);
        assertEquals(testNode3, testNode2.getParent());
    }

    @Test 
    public void getIndentTestFirstNode() {
        String result = testNode1.getIndent();

        assertEquals("", result);
    }

    @Test 
    public void getIndentTestTwoDeep() {
        String result = child3a.getIndent();

        assertEquals("\t\t", result);
    }

    @Test
    public void addChildTest() {
        testNode2.addChild(testItem2a);
        testNode3.setParent(testNode1);

        assertEquals(testItem2a, testNode2.getChildren().get(0).getItemData());
        assertEquals(testNode2, testNode2.getChildren().get(0).getParent());
        assertEquals(0, testNode2.getChildren().get(0).getChildren().size());
    }

    @Test
    public void removeNodeTest() {
        child2a.removeNode();
        List<Node> expected = new ArrayList<>();

        assertEquals(expected, testNode1.getChildren());
    }

    @Test
    public void removeNodeMultipleChildrenTest() {
        child3a.removeNode();
        List<Node> expected = new ArrayList<>();
        expected.add(child3b);

        assertEquals(expected, child2a.getChildren());
    }

    @Test
    public void allNodesWithCategoryTest() {
        List<Node> result = testNode1.allNodesWithCategory(Category.COMPONENT);

        List<Node> testList = new ArrayList<Node>();
        testList.add(testNode1);
        testList.add(child3a);
        assertEquals(testList, result);
    }

    @Test
    public void allNodesWithCategoryNoneFoundTest() {
        List<Node> result = testNode1.allNodesWithCategory(Category.TECHNOLOGY);

        List<Node> testList = new ArrayList<Node>();
        assertEquals(testList, result);
    }

    @Test
    public void allNodesWithNameFirstNodeTest() {
        List<Node> result = testNode1.allNodesWithName("Item1");
        List<Node> testList = new ArrayList<Node>();
        testList.add(testNode1);
        assertEquals(testList, result);
    }

    @Test
    public void allNodesWithNameTwoDeepTest() {
        List<Node> result = testNode1.allNodesWithName("Item4");
        List<Node> testList = new ArrayList<Node>();
        testList.add(child3b);
        assertEquals(testList, result);
    }

    @Test
    public void allNodesWithNameDoesNotExistTest() {
        List<Node> result = testNode1.allNodesWithName("Item27");
        assertTrue(result.size() == 0);
    }

    @Test
    public void allNodesWithNameMultipleNamesTest() {
        Node child3c = child2a.addChild(testItem2a);

        List<Node> result = testNode1.allNodesWithName("Item2");
        List<Node> testList = new ArrayList<Node>();
        testList.add(child2a);
        testList.add(child3c);
        assertEquals(testList, result);
    }

    @Test
    public void getBaseComponentsOneNode() {
        List<Node> result = testNode2.getBaseComponents();
        List<Node> testList = new ArrayList<Node>();
        testList.add(testNode2);
        assertEquals(testList, result);
    }

    @Test
    public void getBaseComponentsTwoDeepTest() {
        List<Node> result = testNode1.getBaseComponents();
        List<Node> testList = new ArrayList<Node>();
        testList.add(child3a);
        testList.add(child3b);
        assertEquals(testList, result);
    }

    @Test
    public void getListOfNodesTest() {
        List<Node> result = testNode1.getListOfNodes();
        List<Node> testList = new ArrayList<Node>();
        testList.add(testNode1);
        testList.add(child2a);
        testList.add(child3a);
        testList.add(child3b);
        assertEquals(testList, result);
    }

    @Test
    public void getListOfNodesOneNodeTest() {
        List<Node> result = testNode2.getListOfNodes();
        List<Node> testList = new ArrayList<Node>();
        testList.add(testNode2);
        assertEquals(testList, result);
    }

    @Test
    public void toStringTest() {
        testItem1 = new Item("Item1", Category.COMPONENT, Method.REFINE, 1, 100);
        String expected = "1 Item1";
        String result = testNode2.toString();
        assertEquals(expected, result);
    }
}
