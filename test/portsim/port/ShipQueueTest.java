package portsim.port;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import portsim.cargo.Cargo;
import portsim.ship.BulkCarrier;
import portsim.ship.ContainerShip;
import portsim.ship.NauticalFlag;
import portsim.ship.Ship;
import portsim.util.BadEncodingException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ShipQueueTest {
    private ShipQueue shipQueue;
    private BulkCarrier bulkCarrier1;
    private BulkCarrier bulkCarrier3;
    private BulkCarrier shipCarryingDangerousCargo;
    private BulkCarrier shipRequireMedicalAssistance;
    private BulkCarrier shipReadyToDock;
    private List<Ship> shipQueueList;
    private ContainerShip containerShip;
    private ShipQueue shipQueue2;
    private ShipQueue shipQueue3;
    private ShipQueue shipQueue4;
    private ShipQueue shipQueue5;
    private ShipQueue shipQueue6;

    @Before
    public void setUp() throws Exception {
        this.shipQueue = new ShipQueue();
        this.shipQueue2 = new ShipQueue();
        this.shipQueue3 = new ShipQueue();
        this.shipQueue4 = new ShipQueue();
        this.shipQueue5 = new ShipQueue();
        this.shipQueue6 = new ShipQueue();
        this.shipQueueList = new ArrayList<>();
        this.bulkCarrier1 = new BulkCarrier(3456789, "Glorious", "Switzerland",
                NauticalFlag.NOVEMBER, 120);
        this.containerShip = new ContainerShip(1234567, "aBulkCarrier", "China",
                NauticalFlag.BRAVO, 119);
        this.bulkCarrier3 = new BulkCarrier(7654321, "FlyingDutchman", "Unknown",
                NauticalFlag.BRAVO, 500);
        this.shipCarryingDangerousCargo = new BulkCarrier(1357924, "A",
                "China", NauticalFlag.BRAVO, 100);
        this.shipRequireMedicalAssistance = new BulkCarrier(1111111,"Cat",
                "China", NauticalFlag.WHISKEY, 99);
        this.shipReadyToDock = new BulkCarrier(2222222, "Dog",
                "China", NauticalFlag.HOTEL, 101);
    }

    @Test
    public void addShipTest() {
        shipQueue.add(bulkCarrier1);
        shipQueueList.add(bulkCarrier1);
        for (Ship ship : shipQueue.getShipQueue()) {
            for (Ship ship1 : shipQueueList) {
                assertEquals(ship, ship1);
            }
        }
    }


    @Test
    public void equalsTest() {
        shipQueue.add(bulkCarrier1);
        shipQueue2.add(bulkCarrier1);
        assertTrue(shipQueue.equals(shipQueue2) && shipQueue2.equals(shipQueue));
        assertTrue(shipQueue.hashCode() == shipQueue2.hashCode());
    }

    @Test
    public void equalNullTest() {
        ShipQueue nullShipQueue = null;
        assertFalse(shipQueue.equals(nullShipQueue) && nullShipQueue.equals(shipQueue));
    }

    @Test
    public void peekBasicTest() {
        shipQueue2.add(shipCarryingDangerousCargo);
        shipQueue.add(shipReadyToDock);
        shipQueue3.add(shipRequireMedicalAssistance);
        shipQueue4.add(containerShip);
        shipQueue5.add(bulkCarrier1);
        assertEquals(shipCarryingDangerousCargo, shipQueue2.peek());
        assertEquals(shipReadyToDock, shipQueue.peek());
        assertEquals(shipRequireMedicalAssistance, shipQueue3.peek());
        assertEquals(containerShip, shipQueue4.peek());
        assertEquals(bulkCarrier1, shipQueue5.peek());
        assertNull(shipQueue6.peek());
    }

    @Test
    public void peekReturnOrderTest() {
        shipQueue3.add(shipCarryingDangerousCargo);
        shipQueue3.add(bulkCarrier3);

        shipQueue.add(shipRequireMedicalAssistance);
        shipQueue.add(shipCarryingDangerousCargo);
        assertEquals(shipCarryingDangerousCargo, shipQueue3.peek());
        assertEquals(shipCarryingDangerousCargo, shipQueue.peek());
    }

    @Test
    public void pollBasicTest() {
        shipQueue.add(shipRequireMedicalAssistance);
        assertEquals(shipRequireMedicalAssistance, shipQueue.poll());
        assertNull(shipQueue.poll());
    }

    @Test
    public void pollOrderTest() {
        shipQueue.add(shipReadyToDock);
        shipQueue.add(shipCarryingDangerousCargo);
        assertEquals(shipCarryingDangerousCargo, shipQueue.poll());
        assertEquals(shipReadyToDock, shipQueue.poll());
    }

    @Test
    public void encodeTest() {
        shipQueue.add(shipCarryingDangerousCargo);
        assertEquals("ShipQueue:1:1357924", shipQueue.encode());
    }

    @Test
    public void fromStringValidTest() throws BadEncodingException {
        shipQueue.add(shipCarryingDangerousCargo);
        shipQueue.add(shipReadyToDock);
        try {
            this.shipQueue2 = ShipQueue.fromString(shipQueue.encode());
            assertTrue(this.shipQueue2.getShipQueue().contains(shipCarryingDangerousCargo) &&
                    this.shipQueue2.getShipQueue().contains(shipReadyToDock));
        } catch (BadEncodingException e) {
            fail("This string is valid, and should not throw an exception: " + shipQueue.encode());
        }
    }

    @Test (expected = BadEncodingException.class)
    public void fromStringInvalidColonTest() throws BadEncodingException {
        String invalidColonString = "ShipQueue:1:1234567::";
        this.shipQueue = ShipQueue.fromString(invalidColonString);
    }

    @Test (expected = BadEncodingException.class)
    public void fromStringInvalidStartTest() throws BadEncodingException {
        String invalidStart = "HelloWorld:2:1234567,7654321";
        this.shipQueue = ShipQueue.fromString(invalidStart);
    }

    @Test (expected = BadEncodingException.class)
    public void fromStringInvalidNumOfShipTest() throws BadEncodingException {
        String invalidNumOfShip = "ShipQueue:A:";
        this.shipQueue = ShipQueue.fromString(invalidNumOfShip);
    }

    @Test (expected = BadEncodingException.class)
    public void fromStringInvalidShipTest() throws BadEncodingException {
        String invalidShips = "ShipQueue:1:1234567,7654321";
        this.shipQueue = ShipQueue.fromString(invalidShips);
    }

    @Test (expected = BadEncodingException.class)
    public void fromStringInvalidImoNumberTest() throws BadEncodingException {
        String invalidImoNumber = "ShipQueue:1:123456seven";
        this.shipQueue = ShipQueue.fromString(invalidImoNumber);
    }

    @Test (expected = BadEncodingException.class)
    public void fromStringNoSuchShipTest() throws BadEncodingException {
        String noSuchShip = "ShipQueue:1:1234567";
        this.shipQueue = ShipQueue.fromString(noSuchShip);
    }



    @After
    public void tearDown() throws Exception {
        Ship.resetShipRegistry();
        Cargo.resetCargoRegistry();
    }

}