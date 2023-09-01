package portsim.movement;

import portsim.ship.Ship;

/**
 * The class representing the movement of a ship coming into or out of the port.
 */
public class ShipMovement extends Movement {
    /**
     * The different type of directions the ships will move.
     */
    private String direction;

    /**
     * A number representing the time taken for the ship movement.
     */
    private long time;

    /**
     * The ship which is the Ship type itself.
     */
    private final Ship ship;

    /**
     * Constructs a new ship movement with the given action time \n
     * and direction to be undertaken with the given ship.
     *
     * @param time      the time taken for the ship movements.
     * @param direction the direction of the ship movements.
     * @param ship      the object ship itself.
     */
    public ShipMovement(long time, MovementDirection direction, Ship ship) {
        super(time, direction);
        this.ship = ship;
    }

    /**
     * Returns the ship undertaking the movement.
     *
     * @return the ship undertaking the movement.
     */
    public Ship getShip() {
        return ship;
    }

    /**
     * Creates the human-readable string representation of this ShipMovement.
     * The format of the string to return is
     * DIRECTION ShipMovement to occur at time involving the ship name
     * Where:
     * - DIRECTION is the direction of the movement.
     * - time is the time the movement is meant to occur.
     * - name is the name of the ship that is being moved.
     *
     * @return the human-readable string representation of this ShipMovement.
     */
    @Override
    public String toString() {
        return direction + " ShipMovement " + "to occur at " + time
                + "involving the ship" + this.getShip().getName();
    }
}

