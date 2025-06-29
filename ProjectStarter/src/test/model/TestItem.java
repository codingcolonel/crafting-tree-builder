package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestItem {
    private Item testItem;
    private Item testItem1;
    private Item testItem2;

    @BeforeEach
    void runBefore() {
        testItem = new Item("Item", Category.COMPONENT, Method.REFINE, 1, 100);
        testItem1 = new Item(testItem);
        testItem2 = new Item("Another Item", Category.COMPONENT, Method.REFINE, 8, 1000);
    }

    @Test
    void constructorTest() {
        assertEquals("Item", testItem.getName());
        assertEquals(Category.COMPONENT, testItem.getCategory());
        assertEquals(Method.REFINE, testItem.getMethod());
        assertEquals(100, testItem.getCost());
    }

    @Test
    void copyConstructorTest() {
        assertEquals("Item", testItem1.getName());
        assertEquals(Category.COMPONENT, testItem1.getCategory());
        assertEquals(Method.REFINE, testItem1.getMethod());
        assertEquals(100, testItem1.getCost());
    }

    @Test
    void settersTest() {
        testItem.setName("Oxygen");
        assertEquals("Oxygen", testItem.getName());
        testItem.setCategory(Category.CONSUMABLE);
        assertEquals(Category.CONSUMABLE, testItem.getCategory());
        testItem.setMethod(Method.COOK);
        assertEquals(Method.COOK, testItem.getMethod());
        testItem.setAmount(10);
        assertEquals(10, testItem.getAmount());
        testItem.setCost(200);
        assertEquals(200, testItem.getCost());
    }

    @Test
    public void addSubractMultiplyDivideAmountTest() {
        testItem.multiplyAmount(6);
        assertEquals(6, testItem.getAmount());
        testItem.divideAmount(3);
        assertEquals(2, testItem.getAmount());
        testItem.addAmount(6);
        assertEquals(8, testItem.getAmount());
        testItem.substractAmount(3);
        assertEquals(5, testItem.getAmount());
    }

    @Test
    public void addSubractMultiplyDivideCostTest() {
        testItem.multiplyCost(6);
        assertEquals(600, testItem.getCost());
        testItem.divideCost(3);
        assertEquals(200, testItem.getCost());
        testItem.addCost(600);
        assertEquals(800, testItem.getCost());
        testItem.substractCost(300);
        assertEquals(500, testItem.getCost());
    }

    @Test
    public void equalsTest() { 
        assertTrue(testItem.equals(testItem1));
    }

    @Test
    public void notEqualsTest() { 
        assertFalse(testItem.equals(testItem2));
    }
}
