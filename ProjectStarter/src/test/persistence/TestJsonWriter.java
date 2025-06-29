package persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Category;
import model.Item;
import model.Method;
import model.Node;
import model.TreeManager;

public class TestJsonWriter {
    private JsonWriter testJsonWriter;
    private JsonWriter testJsonWriter2;

    private JsonReader testJsonReader;

    private TreeManager testTreeManager;

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
        testJsonWriter = new JsonWriter("data/writingTestData.json");
        testJsonWriter2 = new JsonWriter("W:/thisbetternotexist");

        testJsonReader = new JsonReader("data/writingTestData.json");

        testTreeManager = new TreeManager();

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
    public void openValidFileTest() {
        try {
            testJsonWriter.open();
            testJsonWriter.close();
        } catch (FileNotFoundException e) {
            fail();
        }
    }

    @Test
    public void openInvalidFileTest() {
        try {
            testJsonWriter2.open();
            fail();
            testJsonWriter2.close();
        } catch (FileNotFoundException e) {
            // test passed
        }
    }

    @Test
    public void writeToFileTest() {
        testTreeManager.addToSavedTrees(testNode1);
        testTreeManager.addToSavedTrees(testNode2);

        try {
            testJsonWriter.open();
            testJsonWriter.write(testTreeManager);
            testJsonWriter.close();
            List<Node> result = testJsonReader.read();
            List<Node> expected = testTreeManager.getSavedTrees();
            TestJsonReader.checkEquivalence(result.get(0), expected.get(0));
            TestJsonReader.checkEquivalence(result.get(1), expected.get(1));
        } catch (Exception e) {
            System.err.println(e);
            fail();
        }
    }

    @Test
    public void writeEmptyToFileTest() {
        try {
            testJsonWriter.open();
            testJsonWriter.write(testTreeManager);
            testJsonWriter.close();
            List<Node> result = testJsonReader.read();
            List<Node> expected = new ArrayList<Node>();
            assertEquals(expected, result);
        } catch (Exception e) {
            fail();
        }
    }
}
