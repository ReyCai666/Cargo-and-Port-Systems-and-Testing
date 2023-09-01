package portsim.movement;

import portsim.cargo.Cargo;

import java.util.ArrayList;
import java.util.List;

/**
 * The class representing the movement of cargo coming into or out of the port.
 */
public class CargoMovement extends Movement {
    /**
     * A number representing the movement time taken \n
     * for the cargos coming out or into the port.
     */
    private long time;

    /**
     * The different type of directions the cargos will move.
     */
    private MovementDirection direction;

    /**
     * A list containing all the cargos which will be moved.
     */
    private final List<Cargo> cargo;

    /**
     * Constructs a new cargo movement with the given action time \n
     * and direction to be undertaken with the given cargo.
     *
     * @param time      the time taken for the cargo movement.
     * @param direction the direction of the cargo movement.
     * @param cargo     the list containing all the cargos which will be moved.
     */
    public CargoMovement(long time, MovementDirection direction,
                         List<Cargo> cargo) {
        super(time, direction);
        this.cargo = new ArrayList<>();
    }

    /**
     * Returns the cargo that will be moved.
     *
     * @return Returns the cargo that will be moved.
     */
    public List<Cargo> getCargo() {
        return cargo;
    }

    /**
     * Creates the string representation of this CargoMovement.
     * The format of the string to return is:
     * DIRECTION CargoMovement to occur at time involving num piece(s) of cargo
     * Where:
     * - DIRECTION is the direction of the movement.
     * - time is the time the movement is meant to occur.
     * - num is the number of cargo pieces that are being moved.
     *
     * @return the human-readable string representation of this CargoMovement.
     */
    @Override
    public String toString() {
        return direction + " CargoMovement " + "to occur at " + time
                + " involving " + cargo.size()
                + " piece(s) of cargo";
    }
}
