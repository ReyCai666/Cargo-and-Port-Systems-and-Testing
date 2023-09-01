package portsim.cargo;

// add any required imports here
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BulkCargoTest {

    private BulkCargo bulkCargoSample;

    @Before
    public void setUp() {
        this.bulkCargoSample = new BulkCargo(1, "China", 11111, BulkCargoType.COAL);
    }

    // add unit tests here
    @Test
    public void constructorTest() {
        assertEquals(1, bulkCargoSample.getId());
        assertEquals("China", bulkCargoSample.getDestination());
        assertEquals(11111, bulkCargoSample.getTonnage());
        assertEquals(BulkCargoType.COAL, bulkCargoSample.getType());
    }

    @Test (expected = IllegalArgumentException.class)
    public void negativeIdTest() {
        bulkCargoSample = new BulkCargo(-1, "China", 11111, BulkCargoType.COAL);
    }

    @Test (expected = IllegalArgumentException.class)
    public void negativeTonnageTest() {
        bulkCargoSample = new BulkCargo(1, "China", -11111, BulkCargoType.COAL);
    }

    @Test
    public void getTonnageTest() {
        assertEquals(11111, bulkCargoSample.getTonnage());
    }

    @Test
    public void getTypeTest() {
        assertEquals(BulkCargoType.COAL, bulkCargoSample.getType());
    }

    @Test
    public void toStringTest() {
        assertEquals("BulkCargo 1 to China [COAL - 11111]", bulkCargoSample.toString());
    }

    @After
    public void tearDown() {
        bulkCargoSample = null;
    }
}