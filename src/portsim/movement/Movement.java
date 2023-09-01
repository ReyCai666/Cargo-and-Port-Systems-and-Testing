package portsim.movement;

/**
 * The movement of ships or cargo coming \n
 * into or out of the port from land or sea.
 */
public abstract class Movement {
    /**
     * A number representing the movement time taken \n
     * for the ships or cargos coming out or into the port.
     */
    private final long time;

    /**
     * The different type of directions the ships or cargos will move.
     */
    private final MovementDirection direction;

    /**
     * Constructs a new movement with the given action time and direction.
     *
     * @param time      the movement time taken for the ships/cargos.
     * @param direction the movement directions that the \n
     *                  ships/cargos are moving towards.
     */
    public Movement(long time, MovementDirection direction) {
        this.time = time;
        this.direction = direction;
    }

    /**
     * Returns the time the movement should be actioned.
     *
     * @return the time the movement should be actioned.
     */
    public long getTime() {
        return time;
    }

    /**
     * Returns the direction of the movement.
     *
     * @return the direction of the movement.
     */
    public MovementDirection getDirection() {
        return direction;
    }

    /**
     * Creates the string representation of this Movement.
     * The format of the string to return is
     * DIRECTION MovementClass to occur at time
     * Where:
     * - DIRECTION is the direction of the movement.
     * - MovementClass is the Movement class name.
     * - time is the time the movement is meant to occur.
     *
     * @return the human-readable string representation of this Movement.
     */
    @Override
    public String toString() {
        return direction + " Movement " + "to occur at " + time;
    }
}
