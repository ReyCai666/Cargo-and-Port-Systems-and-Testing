package portsim.port;

import portsim.ship.ContainerShip;
import portsim.ship.NauticalFlag;
import portsim.ship.Ship;
import portsim.util.BadEncodingException;
import portsim.util.Encodable;
import portsim.util.NoSuchShipException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Queue of ships waiting to enter a Quay at the port. Ships are
 * chosen based on their priority.
 */
public class ShipQueue implements Encodable {

    /**
     * A list containing all the ships currently stored in this ShipQueue.
     */
    private ArrayList<Ship> shipInQueue;

    /**
     * Constructs a new ShipQueue with an initially empty queue of ships.
     */
    public ShipQueue() {
        shipInQueue = new ArrayList<>();

    }

    /**
     * Adds the specified ship to the queue.
     *
     * @param ship to be added to queue
     */
    public void add(Ship ship) {
        shipInQueue.add(ship);
    }

    /**
     * Gets the next ship to enter the port and removes it from the queue.
     * <p>
     * The same rules as described in peek() should be used for determining
     * which ship to remove and return.
     *
     * @return next ship to dock
     */
    public Ship poll() {
        Ship ship = this.peek();
        shipInQueue.remove(ship);
        return ship;
    }

    /**
     * Returns the next ship waiting to enter the port. The queue should not change.
     * <p>
     * The rules for determining which ship in the queue should be returned next
     * are as follows:
     * <ol>
     *     <li>If a ship is carrying dangerous cargo, it should be returned. If more
     *     than one ship is carrying dangerous cargo return the one added to the
     *     queue first.</li>
     *     <li>If a ship requires medical assistance, it should be returned. If more
     *     than one ship requires medical assistance, return the one added to
     *     the queue first.</li>
     *     <li>If a ship is ready to be docked, it should be returned. If more than one
     *     ship is ready to be docked, return the one added to the queue first.</li>
     *     <li>If there is a container ship in the queue, return the one added to the
     *     queue first.</li>
     *     <li>If this point is reached and no ship has been returned, return the ship
     *     that was added to the queue first.</li>
     *     <li>If there are no ships in the queue, return null.</li>
     * </ol>
     *
     * @return next ship in queue
     */
    public Ship peek() {
        LinkedList<Ship> newShipLst = new LinkedList<>();

        for (Ship ship : shipInQueue) {
            if (ship.getFlag().equals(NauticalFlag.BRAVO)) {
                return ship;
            }
        }
        for (Ship ship : shipInQueue) {
            if (ship.getFlag().equals(NauticalFlag.WHISKEY)) {
                return ship;
            }
        }
        for (Ship ship : shipInQueue) {
            if (ship.getFlag().equals(NauticalFlag.HOTEL)) {
                return ship;
            }
        }
        for (Ship ship : shipInQueue) {
            if (ship instanceof ContainerShip) {
                return ship;
            } else {
                return shipInQueue.get(0);
            }
        }
        return null;
    }

    /**
     * Returns a list containing all the ships currently stored in this ShipQueue.
     * <p>
     * The order of the ships in the returned list should be the order in which
     * the ships were added to the queue.
     * <p>
     * Adding or removing elements from the returned list should not affect the
     * original list.
     *
     * @return ships in queue
     */
    public List<Ship> getShipQueue() {
        return new ArrayList<>(shipInQueue);
    }

    /**
     * Returns true if and only if this ship queue is equal to the other given ship queue.
     * <p>
     * For two ship queue to be equal, they must have the same ships in the queue,
     * in the same order.
     *
     * @param o other object to check equality
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        ShipQueue shipQueue = (ShipQueue) o;
        return this.shipInQueue.equals(shipQueue.shipInQueue);
    }

    /**
     * Returns the hash code of this ship queue.
     * <p>
     * Two ship queue's that are equal according to equals(Object)
     * method should have the same hash code.
     *
     * @return hash code of this ship queue.
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (this.shipInQueue == null ? 0
                : this.shipInQueue.hashCode());
        return result;
    }

    /**
     * Returns the machine-readable string representation of this ShipQueue.
     * <p>
     * The format of the string to return is
     * <pre>ShipQueue:numShipsInQueue:shipID,shipID,...</pre>
     * Where:
     * <ul>
     *     <li>{@code numShipsInQueue} is the total number of ships in the ship
     *     queue in the port</li>
     *     <li>{@code shipID} is each ship's ID in the aforementioned queue if
     *     present (numShipsInQueue > 0)</li>
     * </ul>
     * <p>
     * For example: <pre>ShipQueue:0:</pre> or
     * <pre>ShipQueue:2:3456789,1234567</pre>
     *
     * @return encoded string representation of this ShipQueue
     */
    @Override
    public String encode() {
        StringBuilder result = new StringBuilder();
        StringBuilder shipId = new StringBuilder();
        for (int n = 0; n < shipInQueue.size() - 1; n++) {
            shipId.append(shipInQueue.get(n).getImoNumber()).append(",");
        }
        shipId.append(shipInQueue.get(shipInQueue.size() - 1).getImoNumber());
        result.append("ShipQueue:").append(shipInQueue.size()).append(":")
                .append(shipId);
        return result.toString();
    }

    /**
     * Creates a ship queue from a string encoding.
     * <p>
     * The format of the string should match the encoded representation
     * of a ship queue, as described in encode().
     * <p>
     * The encoded string is invalid if any of the following conditions are true:
     * <ul>
     *     <li>The number of colons (:) detected was more/fewer than expected.</li>
     *     <li>The string does not start with the literal string "ShipQueue"</li>
     *     <li>The number of ships in the shipQueue is not an integer
     *     (i.e. cannot be parsed by Integer.parseInt(String)).</li>
     *     <li>The number of ships in the shipQueue does not match the number
     *     specified.</li>
     *     <li>The imoNumber of the ships in the shipQueue are not valid longs.
     *     (i.e. cannot be parsed by Long.parseLong(String)).</li>
     *     <li>Any imoNumber read does not correspond to a valid ship in the simulation</li>
     * </ul>
     *
     * @param string string containing the encoded ShipQueue
     * @return decoded ship queue instance
     * @throws BadEncodingException if the format of the given string is invalid
     *                              according to the rules above
     */
    public static ShipQueue fromString(String string)
            throws BadEncodingException {
        int colonCount = 0;
        final ShipQueue shipQueue = new ShipQueue();
        // The number of colons (:) detected was more/fewer than expected.
        for (int i = 0; i < string.length(); i++) {
            char character = string.charAt(i);
            if (character == ':') {
                colonCount++;
            }
        }
        if (colonCount != 2) {
            throw new BadEncodingException();
        }
        int firstColonIndex = string.indexOf(":");
        // The string does not start with the literal string "ShipQueue"
        if (!(string.substring(0, firstColonIndex).equals(ShipQueue.class.getSimpleName()))) {
            throw new BadEncodingException();
        }
        //The number of ships in the shipQueue is not an integer.

        String newString = string.substring(10);
        int secondColonIndex = newString.indexOf(":") + 1;
        secondColonIndex = firstColonIndex + secondColonIndex;
        String numberOfShip = string.substring(firstColonIndex + 1,
                secondColonIndex);
        try {
            Integer.parseInt(numberOfShip);
        } catch (NumberFormatException e) {
            throw new BadEncodingException();
        }
        // The number of ships in the shipQueue does not match the number specified.
        ArrayList<String> shipQueueElements = new ArrayList<>(
                Arrays.asList(string.split(":")));
        if (shipQueueElements.size() == 3) {
            ArrayList<String> imoNumbers = new ArrayList<>(
                    Arrays.asList(shipQueueElements.get(2).split(",")));
            if (Integer.parseInt(numberOfShip) != imoNumbers.size()) {
                throw new BadEncodingException();
            }
            // The imoNumber of the ships in the shipQueue are not valid longs.
            for (int n = 0; n < imoNumbers.size(); n++) {
                try {
                    Long.parseLong(imoNumbers.get(n));
                } catch (NumberFormatException e) {
                    throw new BadEncodingException();
                }
                try {
                    Ship.getShipByImoNumber(Long.parseLong(imoNumbers.get(0)));
                } catch (NoSuchShipException e) {
                    throw new BadEncodingException();
                }
            }
        }
        return shipQueue;
    }
}
