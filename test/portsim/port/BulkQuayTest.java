package portsim.port;

// add any required imports here

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import portsim.ship.BulkCarrier;
import portsim.ship.NauticalFlag;


import static org.junit.Assert.*;

public class BulkQuayTest {

    private BulkCarrier bulkCarrierSample;
    private BulkQuay bulkQuaySample;

    // add unit tests here

    @Before
    public void setUp() {
        this.bulkQuaySample = new BulkQuay(66, 66666);
        this.bulkCarrierSample = new BulkCarrier(2325336, "aShip", "China",
                NauticalFlag.BRAVO, 6666);
    }

    @Test
    public void constructorTest() {
        assertEquals(66, bulkQuaySample.getId());
        assertEquals(66666, bulkQuaySample.getMaxTonnage());
    }

    @Test
    public void getIdTest() {
        assertEquals(66, bulkQuaySample.getId());
    }

    @Test (expected = IllegalArgumentException.class)
    public void negativeBulkQuayIdTest() {
        bulkQuaySample = new BulkQuay(-66, 66666);
    }

    @Test
    public void getMaxTonnageTest() {
        assertEquals(66666, bulkQuaySample.getMaxTonnage());
    }

    @Test (expected = IllegalArgumentException.class)
    public  void negativeMaxTonnageTest() {
        bulkQuaySample = new BulkQuay(66, -66666);
    }

    @Test
    public void toStringTest() {
        bulkQuaySample.shipArrives(bulkCarrierSample);
        assertEquals("BulkQuay 66 [Ship: 2325336] - 66666", bulkQuaySample.toString());
    }

    @Test
    public void toStringEmptyQuayTest() {
        assertEquals("BulkQuay 66 [Ship: None] - 66666", bulkQuaySample.toString());
    }

    @After
    public void tearDown() {
        bulkQuaySample = null;
        bulkCarrierSample = null;
    }

}