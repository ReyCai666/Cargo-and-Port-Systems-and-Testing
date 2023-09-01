package portsim.ship;

// add any required imports here

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import portsim.cargo.BulkCargo;
import portsim.cargo.BulkCargoType;
import portsim.cargo.Container;
import portsim.cargo.ContainerType;
import portsim.port.BulkQuay;
import portsim.port.ContainerQuay;
import portsim.util.NoSuchCargoException;

import static org.junit.Assert.*;

public class BulkCarrierTest {

    private BulkQuay bulkQuaySample;
    private BulkCarrier bulkCarrierSample;
    private BulkCargo bulkCargoSample;
    // add unit tests here

    @Before
    public void setUp() {
        this.bulkCarrierSample = new BulkCarrier(6666666, "ABulkCarrier", "China",
                NauticalFlag.NOVEMBER, 666);
        this.bulkQuaySample = new BulkQuay(66, 666);
        this.bulkCargoSample = new BulkCargo(6, "China", 666, BulkCargoType.GRAIN);
    }

    @Test
    public void constructorTest() {
        assertEquals(6666666, bulkCarrierSample.getImoNumber());
        assertEquals("ABulkCarrier", bulkCarrierSample.getName());
        assertEquals("China", bulkCarrierSample.getOriginFlag());
        assertEquals(NauticalFlag.NOVEMBER, bulkCarrierSample.getFlag());
    }

    // constructor imoNumber test
    @Test (expected = IllegalArgumentException.class)
    public void negativeImoNumberTest() {
        bulkCarrierSample = new BulkCarrier(-6666666, "ABulkCarrier", "China",
                    NauticalFlag.NOVEMBER, 666);
    }

    @Test (expected = IllegalArgumentException.class)
    public void invalidLengthImoNumberTest() {
        bulkCarrierSample = new BulkCarrier(666666666, "ABulkCarrier", "China",
                NauticalFlag.NOVEMBER, 666);
    }

    @Test (expected = IllegalArgumentException.class)
    public void invalidCapacityTest() {
        bulkCarrierSample = new BulkCarrier(6666666, "ABulkCarrier", "China",
                NauticalFlag.NOVEMBER, -1000);
    }

    // canDock Test.
    @Test
    public void wrongTypeOfQuayTest() {
        ContainerQuay containerQuayFaultySample = new ContainerQuay(7, 555);
        assertFalse(bulkCarrierSample.canDock(containerQuayFaultySample));
    }


    @Test
    public void canDockTest() {
        assertTrue(bulkCarrierSample.canDock(bulkQuaySample));
    }

    // canLoad Test.
    @Test
    public void wrongTypeOfCargoTest() {
        Container containerSample = new Container(6, "China", ContainerType.OTHER);
        assertFalse(bulkCarrierSample.canLoad(containerSample));
    }

    @Test
    public void emptyShipBoardTest() {
        assertNull(bulkCarrierSample.getCargo());
    }

    @Test
    public void invalidTonnageTest() {
        BulkCargo bulkCargoFaultySample = new BulkCargo(6, "China", 66666, BulkCargoType.GRAIN);
        assertFalse(bulkCarrierSample.canLoad(bulkCargoFaultySample));
    }

    @Test
    public void wrongDestinationTest() {
        BulkCargo bulkCargoFaultySample2 = new BulkCargo(6, "USA", 666, BulkCargoType.GRAIN);
        assertFalse(bulkCarrierSample.canLoad(bulkCargoFaultySample2));
    }

    @Test
    public void canLoadTest() {
        BulkCargo bulkCargoFaultySample3 = new BulkCargo(6, "China", 666, BulkCargoType.GRAIN);
        BulkCarrier bulkCarrierSample2 = new BulkCarrier(6666666, "BulkCarrier", "China",
                NauticalFlag.NOVEMBER, 6666);

        assertTrue(bulkCarrierSample2.canLoad(bulkCargoFaultySample3));
    }

    // loadCargo test.
    @Test
    public void loadCargoTest() {
        bulkCarrierSample.loadCargo(bulkCargoSample);
        assertEquals(bulkCargoSample, bulkCarrierSample.getCargo());
    }

    // unloadCargo test.
    @Test
    public void unloadedShipTest() {
        try {
            bulkCarrierSample.unloadCargo();
            fail();
        } catch (NoSuchCargoException ignored) {
        }
    }

    @Test
    public void unloadCargoTest() throws NoSuchCargoException {
        bulkCarrierSample.loadCargo(bulkCargoSample);
        assertEquals(bulkCargoSample, bulkCarrierSample.unloadCargo());
    }


    // getCargo test.
    @Test
    public void getCargoTest() {
        bulkCarrierSample.loadCargo(bulkCargoSample);
        assertEquals(bulkCargoSample, bulkCarrierSample.getCargo());
    }

    // toString test.
    @Test
    public void toStringTest() {
        bulkCarrierSample.loadCargo(bulkCargoSample);
        assertEquals("BulkCarrier ABulkCarrier from China [NOVEMBER] carrying GRAIN",
                bulkCarrierSample.toString());
    }

    @Test
    public void toStringNoCargoTest() {
        assertEquals("BulkCarrier ABulkCarrier from China [NOVEMBER] carrying nothing",
                bulkCarrierSample.toString());
    }

    @After
    public void tearDown() {
        bulkCarrierSample = null;
        bulkCargoSample = null;
        bulkQuaySample = null;
    }
}