package portsim.port;

import portsim.cargo.BulkCargo;
import portsim.cargo.Cargo;
import portsim.cargo.Container;
import portsim.evaluators.*;
import portsim.movement.CargoMovement;
import portsim.movement.Movement;
import portsim.movement.MovementDirection;
import portsim.movement.ShipMovement;
import portsim.ship.BulkCarrier;
import portsim.ship.ContainerShip;
import portsim.ship.Ship;
import portsim.util.BadEncodingException;
import portsim.util.Encodable;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * A place where ships can come and dock with Quays to load / unload their
 * cargo.
 * <p>
 * Ships can enter a port through its queue. Cargo is stored within the port at warehouses.
 *
 * @ass1_partial
 */
public class Port implements Encodable {

    /**
     * The name of this port used for identification.
     */
    private String name;
    /**
     * The quays associated with this port.
     */
    private List<Quay> quays = new ArrayList<>();
    /**
     * The cargo currently stored at the port at warehouses. Cargo unloaded from
     * trucks / ships
     */
    private List<Cargo> storedCargo;

    /**
     * The list of evaluators at the port.
     */
    private List<StatisticsEvaluator> statisticsEvaluators;

    /**
     * The queue of movements waiting to be processed.
     */
    private PriorityQueue<Movement> priorityQueue;

    /**
     * The time since simulation started.
     */
    private Long time;

    /**
     * The shipQueue of the port.
     */
    private ShipQueue shipQueue = new ShipQueue();

    /**
     * Creates a new port with the given name.
     * <p>
     * The time since the simulation was started should be initialised as 0.
     * <p>
     * The list of quays in the port, stored cargo (warehouses) and statistics
     * evaluators should be initialised as empty lists.
     * <p>
     * An empty ShipQueue should be initialised, and a PriorityQueue should be
     * initialised
     * to store movements ordered by the time of the movement
     * (see {@link Movement#getTime()}).
     *
     * @param name name of the port
     * @ass1_partial
     */
    public Port(String name) {
        this.name = name;
        this.time = (long) 0;
        this.storedCargo = new ArrayList<>();
        this.priorityQueue = new PriorityQueue<>(Comparator.comparing(
                Movement::getTime));
        this.statisticsEvaluators = new ArrayList<>();
    }

    /**
     * Creates a new port with the given name, time elapsed, ship queue,
     * quays and stored cargo.
     * <p>
     * The list of statistics evaluators should be initialised as an empty list.
     * <p>
     * A PriorityQueue should be initialised to store movements ordered by the
     * time of the movement (see Movement.getTime()).
     *
     * @param name        name of the port
     * @param time        number of minutes since simulation started
     * @param shipQueue   ships waiting to enter the port
     * @param quays       the port's quays
     * @param storedCargo the cargo stored at the port
     * @throws IllegalArgumentException if time &lt; 0
     */
    public Port(String name, long time, ShipQueue shipQueue, List<Quay> quays,
                List<Cargo> storedCargo) throws IllegalArgumentException {
        this.name = name;
        this.time = time;
        this.quays = quays;
        this.storedCargo = storedCargo;
        this.priorityQueue = new PriorityQueue<Movement>(
                Comparator.comparing(Movement::getTime));
        this.shipQueue = shipQueue;
        this.statisticsEvaluators = new ArrayList<>();
        if (this.time < 0) {
            throw new IllegalArgumentException("The time since simulation "
                    + "start must be greater than"
                    + " or equal to 0: " + this.getTime());
        }
    }

    /**
     * Returns the name of this port.
     *
     * @return port's name
     * @ass1
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a list of all quays associated with this port.
     * <p>
     * Adding or removing elements from the returned list should not affect
     * the original list.
     * <p>
     * The order in which quays appear in this list should be the same as
     * the order in which they were added by calling {@link #addQuay(Quay)}.
     *
     * @return all quays
     * @ass1
     */
    public List<Quay> getQuays() {
        return new ArrayList<>(this.quays);
    }

    /**
     * Returns the cargo stored in warehouses at this port.
     * <p>
     * Adding or removing elements from the returned list should not affect
     * the original list.
     *
     * @return port cargo
     * @ass1
     */
    public List<Cargo> getCargo() {
        return new ArrayList<>(this.storedCargo);
    }

    /**
     * Adds a quay to the ports control.
     *
     * @param quay the quay to add
     * @ass1
     */
    public void addQuay(Quay quay) {
        this.quays.add(quay);
    }

    /**
     * Returns the time since simulation started.
     *
     * @return time in minutes
     */
    public long getTime() {
        for (StatisticsEvaluator evaluators : statisticsEvaluators) {
            return evaluators.getTime();
        }
        return 0;
    }

    /**
     * Adds a movement to the PriorityQueue of movements.
     * <p>
     * If the given movement's action time is less than the current number of minutes
     * elapsed than an IllegalArgumentException should be thrown.
     *
     * @param movement movement to add
     * @throws IllegalArgumentException If the given movement's action time is
     *                                  less than the current number of minutes elapsed
     */
    public void addMovement(Movement movement) throws IllegalArgumentException {
        if (movement.getTime() < this.time) {
            throw new IllegalArgumentException();
        }
        this.priorityQueue.add(movement);
    }

    /**
     * Processes a movement.
     * <p>
     * The action taken depends on the type of movement to be processed.
     * <p>
     * If the movement is a ShipMovement:
     * <ul>
     *     <li>If the movement direction is INBOUND then the ship should be added
     *     to the ship queue.</li>
     *     <li>If the movement direction is OUTBOUND then any cargo stored in the
     *     port whose destination is the ship's origin port should be added to the
     *     ship according to Ship.canLoad(Cargo). Next, the ship should be removed
     *     from the quay it is currently docked in (if any).</li>
     * </ul>
     * If the movement is a CargoMovement:
     * <ul>
     *     <li>If the movement direction is INBOUND then all of the cargo that is being
     *     moved should be added to the port's stored cargo.</li>
     *     <li>If the movement direction is OUTBOUND then all cargo with the given IDs
     *     should be removed from the port's stored cargo.</li>
     * </ul>
     * Finally, the movement should be forwarded onto each statistics evaluator stored
     * by the port by calling StatisticsEvaluator.onProcessMovement(Movement).
     *
     * @param movement movement to execute
     */
    public void processMovement(Movement movement) {
        if (movement instanceof ShipMovement) {
            Ship shipTakenThisMovement = ((ShipMovement) movement).getShip();
            if (movement.getDirection().equals(MovementDirection.INBOUND)) {
                this.getShipQueue().add(shipTakenThisMovement);
            } else if (movement.getDirection().equals(
                    MovementDirection.OUTBOUND)) {
                for (Cargo cargos : this.storedCargo) {
                    if (cargos.getDestination().equals(
                            shipTakenThisMovement.getOriginFlag())) {
                        if (shipTakenThisMovement.canLoad(cargos)) {
                            shipTakenThisMovement.loadCargo(cargos);
                            for (Quay quay : this.getQuays()) {
                                if (quay.getShip() == shipTakenThisMovement) {
                                    quay.shipDeparts();
                                }
                            }
                        }
                    }
                }
            }
        } else if (movement instanceof CargoMovement) {
            List<Cargo> cargoTakenThisMovement = (
                    (CargoMovement) movement).getCargo();
            if (movement.getDirection().equals(MovementDirection.INBOUND)) {
                this.storedCargo.addAll(cargoTakenThisMovement);
            } else if (movement.getDirection().equals(
                    MovementDirection.OUTBOUND)) {
                for (Cargo cargo : cargoTakenThisMovement) {
                    int cargoId = cargo.getId();
                    this.storedCargo.remove(
                            Cargo.getCargoRegistry().get(cargoId));
                }
            }
        }
        for (StatisticsEvaluator evaluator : statisticsEvaluators) {
            evaluator.onProcessMovement(movement);
        }
    }

    /**
     * Returns the queue of ships waiting to be docked at this port.
     *
     * @return port's queue of ships
     */
    public ShipQueue getShipQueue() {
        return this.shipQueue;
    }

    /**
     * Adds the given statistics evaluator to the port's list of evaluators.
     * <p>
     * If the port already has an evaluator of that type, no action should be taken.
     *
     * @param eval statistics evaluator to add to the port
     */
    public void addStatisticsEvaluator(StatisticsEvaluator eval) {
        statisticsEvaluators.add(eval);
        if (statisticsEvaluators.contains(eval)) {
            // No action should be made.
        }
    }

    /**
     * Returns the list of evaluators at the port.
     * <p>
     * Adding or removing elements from the returned list should not affect
     * the original list.
     *
     * @return the ports evaluators
     */
    public List<StatisticsEvaluator> getEvaluators() {
        return statisticsEvaluators;
    }

    /**
     * Returns the queue of movements waiting to be processed.
     *
     * @return movements queue
     */
    public PriorityQueue<Movement> getMovements() {
        return priorityQueue;
    }

    /**
     * Advances the simulation by one minute.
     * <p>
     * On each call to elapseOneMinute(), the following actions should be completed
     * by the port in order:
     * <ol>
     *     <li>Advance the simulation time by 1</li>
     *     <li>If the time is a multiple of 10, attempt to bring a ship from the ship
     *     queue to any empty quay that matches the requirements from Ship.canDock(Quay).
     *     The ship should only be docked to one quay.</li>
     *     <li>If the time is a multiple of 5, all quays must unload the cargo from ships
     *     docked (if any) and add it to warehouses at the port
     *     (the Port's list of stored cargo)</li>
     *     <li>All movements stored in the queue whose action time is equal to the
     *     current time should be processed by processMovement(Movement)</li>
     *     <li>Call StatisticsEvaluator.elapseOneMinute() on all statistics
     *     evaluators</li>
     * </ol>
     */
    public void elapseOneMinute() {
        this.time++;
        if (this.time % 10 == 0) {
            for (Ship ship : this.shipQueue.getShipQueue()) {
                for (Quay quay : this.quays) {
                    if (quay.isEmpty()) {
                        if (ship.canDock(quay)) {
                            quay.shipArrives(ship);
                        }
                    }
                }
            }
        } else if (this.time % 5 == 0) {
            for (Quay quay : this.quays) {
                Ship ship = quay.getShip();
                if (ship instanceof BulkCarrier) {
                    BulkCargo bulkCargo = ((BulkCarrier) ship).getCargo();
                    this.storedCargo.add(bulkCargo);
                } else if (ship instanceof ContainerShip) {
                    this.storedCargo.addAll(((ContainerShip) ship).getCargo());
                    for (Container container : (
                            (ContainerShip) ship).getCargo()) {
                        int containerId = container.getId();
                        Cargo.getCargoRegistry().remove(containerId);
                    }
                }
            }
        }
        for (Movement movements : this.priorityQueue) {
            if (movements.getTime() == this.time) {
                this.processMovement(movements);
            }
        }
        for (StatisticsEvaluator statisticsEvaluator
                : this.statisticsEvaluators) {
            statisticsEvaluator.elapseOneMinute();
        }
    }

    /**
     * Creates a port instance by reading various ship, quay, cargo, movement and
     * evaluator entities from the given reader.
     * <p>
     * The provided file should be in the format:
     * <pre>Name</pre>
     * <pre>Time</pre>
     * <pre>numCargo</pre>
     * <pre>EncodedCargo</pre>
     * <pre>EncodedCargo...</pre>
     * <pre>numShips</pre>
     * <pre>EncodedShip</pre>
     * <pre>EncodedShip...</pre>
     * <pre>numQuays</pre>
     * <pre>EncodedQuay</pre>
     * <pre>EncodedQuay...</pre>
     * <pre>ShipQueue:NumShipsInQueue:shipID,shipId</pre>
     * <pre>StoredCargo:numCargo:cargoID,cargoID</pre>
     * <pre>Movements:numMovements</pre>
     * <pre>EncodedMovement</pre>
     * <pre>EncodedMovement...</pre>
     * <pre>Evaluators:numEvaluators:EvaluatorSimpleName,EvaluatorSimpleName</pre>
     * <p>
     * As specified by encode()
     *
     * @param reader reader from which to load all info
     * @return port created by reading from given reader
     * @throws IOException          if an IOException is encountered when reading from the reader
     * @throws BadEncodingException if the reader reads a line that does not adhere to the rules
     *                              above indicating that the contents of the reader are invalid
     */
    public static Port initialisePort(Reader reader) throws
            IOException, BadEncodingException {
        BufferedReader bufferedReader = new BufferedReader(reader);
        final String line = bufferedReader.readLine();
        String line2 = bufferedReader.readLine();
        try {
            Long.parseLong(line2);
        } catch (NumberFormatException e) {
            throw new BadEncodingException();
        }
        final long timeElapsedPort = Long.parseLong(line2);
        String totalNumCargo = bufferedReader.readLine();
        try {
            Integer.parseInt(totalNumCargo);
        } catch (NumberFormatException e) {
            throw new BadEncodingException();
        }
        int counter = 0;
        List<Cargo> cargoList = new ArrayList<>();
        while (counter < Integer.parseInt(totalNumCargo)) {
            String lines = bufferedReader.readLine();
            try {
                Cargo cargos = Cargo.fromString(lines);
                cargoList.add(cargos);
            } catch (BadEncodingException e) {
                throw new BadEncodingException();
            }
            counter++;
        }
        String numShipsLine = bufferedReader.readLine();
        try {
            Integer.parseInt(numShipsLine);
        } catch (NumberFormatException e) {
            throw new BadEncodingException();
        }
        int counter2 = 0;
        List<Ship> ships = new ArrayList<>();
        ShipQueue shipQueue = new ShipQueue();
        Ship ship;
        while (counter2 < Integer.parseInt(numShipsLine)) {
            String lines = bufferedReader.readLine();
            try {
                ship = Ship.fromString(lines);
                ships.add(ship);
            } catch (BadEncodingException e) {
                throw new BadEncodingException();
            }
            counter2++;
        }
        for (Ship shipInFile : ships) {
            for (int n = 0; n < cargoList.size(); n++) {
                Cargo cargo = cargoList.get(n);
                if (shipInFile.canLoad(cargo)) {
                    shipInFile.loadCargo(cargo);
                    cargoList.remove(cargo);
                }
            }
            shipQueue.add(shipInFile);
        }
        String numQuaysLine = bufferedReader.readLine();
        try {
            Integer.parseInt(numQuaysLine);
        } catch (NumberFormatException e) {
            throw new BadEncodingException();
        }
        int counter3 = 0;
        List<Quay> quayInPort = new ArrayList<>();
        while (counter3 < Integer.parseInt(numQuaysLine)) {
            String lines = bufferedReader.readLine();
            try {
                Quay quay = Quay.fromString(lines);
                quayInPort.add(quay);
            } catch (BadEncodingException e) {
                throw new BadEncodingException();
            }
            counter3++;
        }
        String shipQueueLine = bufferedReader.readLine();
        ArrayList<String> shipQueueElements = new ArrayList<>(
                Arrays.asList(shipQueueLine.split(":")));
        try {
            ShipQueue shipQueueInFile = ShipQueue.fromString(shipQueueLine);
        } catch (BadEncodingException e) {
            throw new BadEncodingException();
        }
        if (shipQueueElements.size() == 3) {
            ArrayList<String> shipIds = new ArrayList<>(
                    Arrays.asList(shipQueueElements.get(2).split(",")));
            for (String shipId : shipIds) {
                try {
                    Long.parseLong(shipId);
                } catch (NumberFormatException e) {
                    throw new BadEncodingException();
                }
                if (!Ship.getShipRegistry().containsKey(
                        Long.parseLong(shipId))) {
                    throw new BadEncodingException();
                }
            }
        }
        String storedCargoLine = bufferedReader.readLine();
        ArrayList<String> storedCargoElements = new ArrayList<>(
                Arrays.asList(storedCargoLine.split(":")));
        String numCargo = storedCargoElements.get(1);
        List<Cargo> storedCargoList = new ArrayList<>();
        try {
            Integer.parseInt(numCargo);
        } catch (NumberFormatException e) {
            throw new BadEncodingException();
        }
        if (storedCargoElements.size() == 3) {
            ArrayList<String> cargoIds = new ArrayList<>(
                    Arrays.asList(storedCargoElements.get(2).split(",")));
            for (String cargoId : cargoIds) {
                try {
                    Integer.parseInt(cargoId);
                } catch (NumberFormatException e) {
                    throw new BadEncodingException();
                }
                if (!Cargo.getCargoRegistry().containsKey(
                        Integer.parseInt(cargoId))) {
                    throw new BadEncodingException();
                }
                Cargo cargo = Cargo.getCargoRegistry().get(Integer.parseInt(cargoId));
                storedCargoList.add(cargo);
            }
        }
        String movementsLine = bufferedReader.readLine();
        ArrayList<String> movementsElement = new ArrayList<>(
                Arrays.asList(movementsLine.split(":")));
        String numMovements = movementsElement.get(1);
        try {
            Integer.parseInt(numMovements);
        } catch (NumberFormatException e) {
            throw new BadEncodingException();
        }
        int counter4 = 0;
        while (counter4 < Integer.parseInt(numMovements)) {
            String lines = bufferedReader.readLine();
            ArrayList<String> encodedMovement = new ArrayList<>(
                    Arrays.asList(lines.split(":")));
            if (encodedMovement.get(0).equals(ShipMovement.class
                    .getSimpleName())) {
                try {
                    ShipMovement shipMovement = ShipMovement.fromString(lines);
                } catch (BadEncodingException e) {
                    throw new BadEncodingException();
                }
            } else if (encodedMovement.get(0).equals(CargoMovement.class
                    .getSimpleName())) {
                try {
                    CargoMovement cargoMovement =
                            CargoMovement.fromString(lines);
                } catch (BadEncodingException e) {
                    throw new BadEncodingException();
                }
            }
            counter4++;
        }
        String evaluatorsLine = bufferedReader.readLine();
        ArrayList<String> encodedEvaluators = new ArrayList<>(
                Arrays.asList(evaluatorsLine.split(":")));
        String numEvaluators = encodedEvaluators.get(1);
        try {
            Integer.parseInt(numEvaluators);
        } catch (NumberFormatException e) {
            throw new BadEncodingException();
        }
        if (encodedEvaluators.size() == 3) {
            ArrayList<String> evaluators = new ArrayList<>(
                    Arrays.asList(encodedEvaluators.get(2).split(",")));
            if (evaluators.size() != Integer.parseInt(numEvaluators)) {
                throw new BadEncodingException();
            }
            for (String evaluator : evaluators) {
                if (!(evaluator.equals(CargoDecompositionEvaluator
                        .class.getSimpleName())
                        || evaluator.equals(QuayOccupancyEvaluator
                        .class.getSimpleName())
                        || evaluator.equals(ShipFlagEvaluator
                        .class.getSimpleName())
                        || evaluator.equals(ShipThroughputEvaluator
                        .class.getSimpleName())
                        || evaluator.equals(StatisticsEvaluator
                        .class.getSimpleName()))) {
                    throw new BadEncodingException();
                }
            }
        }
        return new Port(line, timeElapsedPort, shipQueue, quayInPort,
                storedCargoList);
    }

    /**
     * Returns the machine-readable string representation of this Port.
     * <p>
     * The format of the string to return is
     * <pre>Name</pre>
     * <pre>Time</pre>
     * <pre>numCargo</pre>
     * <pre>EncodedCargo</pre>
     * <pre>EncodedCargo...</pre>
     * <pre>numShips</pre>
     * <pre>EncodedShip</pre>
     * <pre>EncodedShip...</pre>
     * <pre>numQuays</pre>
     * <pre>EncodedQuay</pre>
     * <pre>EncodedQuay...</pre>
     * <pre>ShipQueue:NumShipsInQueue:shipID,shipId</pre>
     * <pre>StoredCargo:numCargo:cargoID,cargoID</pre>
     * <pre>Movements:numMovements</pre>
     * <pre>EncodedMovement</pre>
     * <pre>EncodedMovement...</pre>
     * <pre>Evaluators:numEvaluators:EvaluatorSimpleName,EvaluatorSimpleName</pre>
     * <p>
     *
     * </p>
     * Where:
     * <ul>
     *     <li>Name is the name of the Port</li>
     *     <li>Time is the time elapsed since the simulation started</li>
     *     <li>numCargo is the total number of cargo in the simulation</li>
     *     <li>If present (numCargo > 0): EncodedCargo is the encoded representation
     *     of each individual cargo in the simulation</li>
     *     <li>numShips is the total number of ships in the simulation</li>
     *     <li>If present (numShips > 0): EncodedShip is the encoded representation of
     *     each individual ship encoding in the simulation</li>
     *     <li>numQuays is the total number of quays in the Port</li>
     *     <li>If present (numQuays > 0): EncodedQuay is the encoded representation of
     *     each individual quay in the simulation</li>
     *     <li>numShipsInQueue is the total number of ships in the ship queue
     *     in the port</li>
     *     <li>If present (numShipsInQueue > 0): shipID is each ship's ID in the
     *     aforementioned queue</li>
     *     <li>numCargo is the total amount of stored cargo in the Port</li>
     *     <li>If present (numCargo > 0): cargoID is each cargo's ID in the stored
     *     cargo list of Port</li>
     *     <li>numMovements is the number of movements in the list of movements
     *     in Port</li>
     *     <li>If present (numMovements > 0): EncodedMovement is the encoded
     *     representation of each individual Movement in the
     *     aforementioned list</li>
     *     <li>numEvaluators is the number of statistics evaluators in the
     *     Port evaluators list</li>
     *     <li>If present (numEvaluators > 0): EvaluatorSimpleName is the
     *     name given by Class.getSimpleName() for each evaluator in the aforementioned
     *     list separated by a comma</li>
     *     <li>Each line is separated by a System.lineSeparator()</li>
     * </ul>
     * For example the minimum / default encoding would be:
     * <pre>PortName</pre>
     * <pre>0</pre>
     * <pre>0</pre>
     * <pre>0</pre>
     * <pre>0</pre>
     * <pre>ShipQueue:0:</pre>
     * <pre>StoredCargo:0:</pre>
     * <pre>Movements:0</pre>
     * <pre>Evaluators:0:</pre>
     *
     * @return encoded string representation of this Port
     */
    @Override
    public String encode() {
        int numCargo = Cargo.getCargoRegistry().size();
        int numShip = Ship.getShipRegistry().size();
        int numQuays = this.quays.size();
        final int numMovements = this.priorityQueue.size();
        final ShipQueue shipQueue = new ShipQueue();
        final StringBuilder finalResult = new StringBuilder();
        StringBuilder encodedCargo = new StringBuilder();
        StringBuilder encodedShip = new StringBuilder();
        StringBuilder encodedQuays = new StringBuilder();
        StringBuilder cargoIds = new StringBuilder();
        StringBuilder encodedMovement = new StringBuilder();
        StringBuilder evaluatorsName = new StringBuilder();
        if (numCargo > 0) {
            for (Map.Entry<Integer, Cargo> cargoEntry
                    : Cargo.getCargoRegistry().entrySet()) {
                String cargoString = cargoEntry.getValue().encode()
                        + System.lineSeparator();
                encodedCargo.append(cargoString);
            }
        }
        if (numShip > 0) {
            for (Map.Entry<Long, Ship> shipEntry
                    : Ship.getShipRegistry().entrySet()) {
                String shipString = shipEntry.getValue().encode()
                        + System.lineSeparator();
                encodedShip.append(shipString);
            }
        }
        if (numQuays > 0) {
            for (Quay quay : quays) {
                String quayString = quay.encode() + System.lineSeparator();
                encodedQuays.append(quayString);
            }
        }
        if (numCargo > 0) {
            for (int n = 0; n < this.storedCargo.size() - 1; n++) {
                cargoIds.append(this.storedCargo.get(n)).append(",");
            }
            cargoIds.append(this.storedCargo.get(this.storedCargo.size() - 1));
        }
        String encodedStoredCargo = "StoredCargo:" + this.storedCargo.size()
                + ":" + cargoIds;
        String numMovementsString = "Movements:" + numMovements;
        if (numMovements > 0) {
            for (Movement movement : this.priorityQueue) {
                String movementString = movement.encode()
                        + System.lineSeparator();
                encodedMovement.append(movementString);
            }
        }
        if (statisticsEvaluators.size() > 0) {
            for (int n = 0; n < this.statisticsEvaluators.size() - 1; n++) {
                evaluatorsName.append(this.statisticsEvaluators.get(n)
                        .getClass().getSimpleName()).append(",");
            }
            evaluatorsName.append(this.statisticsEvaluators.get(
                            this.statisticsEvaluators.size() - 1).getClass()
                    .getSimpleName());
        }
        String encodedEvaluators = "Evaluators:"
                + this.statisticsEvaluators.size() + ":" + evaluatorsName;
        finalResult.append(this.name).append(System.lineSeparator())
                .append(this.getTime()).append(System.lineSeparator())
                .append(numCargo).append(System.lineSeparator())
                .append(encodedCargo).append(numShip)
                .append(System.lineSeparator()).append(encodedShip)
                .append(numQuays).append(System.lineSeparator())
                .append(encodedQuays).append(shipQueue.encode())
                .append(System.lineSeparator()).append(encodedStoredCargo)
                .append(System.lineSeparator())
                .append(numMovementsString)
                .append(System.lineSeparator()).append(encodedMovement)
                .append(encodedEvaluators);
        return finalResult.toString();
    }
}
