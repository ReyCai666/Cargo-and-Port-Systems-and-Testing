package portsim.evaluators;

import org.junit.*;

import portsim.cargo.Cargo;
import portsim.movement.CargoMovement;
import portsim.movement.MovementDirection;
import portsim.movement.ShipMovement;
import portsim.ship.BulkCarrier;
import portsim.ship.NauticalFlag;
import portsim.ship.Ship;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ShipThroughputEvaluatorTest {
    private ShipThroughputEvaluator shipThroughputEvaluator;
    private ShipMovement shipMovement;
    private ShipMovement shipMovement2;
    private CargoMovement cargoMovement;

    @Before
    public void setUp() throws Exception {
        Ship.resetShipRegistry();
        this.shipThroughputEvaluator = new ShipThroughputEvaluator();
        BulkCarrier bulkCarrier1 = new BulkCarrier(3456789, "Glorious", "Switzerland",
                NauticalFlag.HOTEL, 120);
        this.shipMovement = new ShipMovement(100, MovementDirection.INBOUND, bulkCarrier1);
        this.shipMovement2 = new ShipMovement(120, MovementDirection.OUTBOUND, bulkCarrier1);
        List<Cargo> bulkCargoList = new ArrayList<>();
        this.cargoMovement = new CargoMovement(130, MovementDirection.INBOUND, bulkCargoList);

    }


    @Test
    public void ShipThroughputEvaluatorConstructorTest() {
        assertEquals(0, shipThroughputEvaluator.getThroughputPerHour());
    }

    @Test
    public void onProcessMovementInboundShipMovementTest() {
        shipThroughputEvaluator.onProcessMovement(shipMovement);
        assertEquals(0, shipThroughputEvaluator.getThroughputPerHour());
    }

    @Test
    public void onProcessMovementOutboundShipMovementTest() {
        shipThroughputEvaluator.onProcessMovement(shipMovement2);
        assertEquals(1, shipThroughputEvaluator.getThroughputPerHour());
    }

    @Test
    public void onProcessMovementCargoMovementTest() {
        shipThroughputEvaluator.onProcessMovement(cargoMovement);
        assertEquals(0, shipThroughputEvaluator.getThroughputPerHour());
    }


    @After
    public void tearDown() throws Exception {
        Ship.resetShipRegistry();
        Cargo.resetCargoRegistry();
    }
}