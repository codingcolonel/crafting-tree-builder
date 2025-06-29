package persistence;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Category;
import model.Item;
import model.Method;
import model.Node;

public class TestJsonReader {
    private JsonReader testJsonReader;
    private JsonReader testJsonReader2;
    
    private Item testItem1;
    private Item testItem2a;
    private Item testItem3a;
    private Item testItem3b;

    private Item testItem2;

    private Node testNode1;
    private Node testNode2;

    private Node child2a;
    private Node child3a;
    private Node child3b;

    @BeforeEach 
    public void runBefore() {
        testJsonReader = new JsonReader("data/readingTestData.json");
        testJsonReader2 = new JsonReader("thisisnotavalidpath");

        testItem1 = new Item("Item1", Category.COMPONENT, Method.REFINE, 1, 100);
        testItem2a = new Item("Item2", Category.CONSUMABLE, Method.CRAFT, 5, 500);
        testItem3a = new Item("Item3", Category.COMPONENT, Method.REFINE, 7, 1000);
        testItem3b = new Item("Item4", Category.CONSUMABLE, Method.CRAFT, 10, 750);

        testItem2 = new Item("Item5", Category.COMPONENT, Method.REFINE, 2, 200);

        testNode1 = new Node(testItem1);
        testNode2 = new Node(testItem2);

        child2a = testNode1.addChild(testItem2a);
        child3a = child2a.addChild(testItem3a);
        child3b = child2a.addChild(testItem3b);
    }

    @Test
    public void readValidFileTest() {
        List<Node> result = null;
        try {
            result = testJsonReader.read();
            checkEquivalence(testNode1, result.get(0));
            checkEquivalence(testNode2, result.get(1));
        } catch (IOException e) {
            fail();
        }
    }


    public static void checkEquivalence(Node node1, Node node2) {
        assertTrue(node1.getItemData().equals(node2.getItemData()));

        List<Node> node1Children = node1.getChildren();
        List<Node> node2Children = node2.getChildren();

        if (node1Children.size() != node2Children.size()) {
            fail();
        }


        if (node1Children.size() != 0) {
            for (int i = 0; i < node1Children.size(); i++) {
                checkEquivalence(node1Children.get(i), node2Children.get(i));
            }
        }
    }

    @Test
    public void readInvalidFileTest() {
        List<Node> result = null;
        try {
            result = testJsonReader2.read();
            fail();
        } catch (IOException e) {
            // test passed
        }
    }
}
