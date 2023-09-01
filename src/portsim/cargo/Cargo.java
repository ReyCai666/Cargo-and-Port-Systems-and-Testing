package portsim.cargo;

/**
 * This class denotes a cargo whose function is \n
 * to be transported via a Ship or land transport.
 */
public abstract class Cargo {
    /**
     * The identifying number of the cargo.
     */
    private final int id;

    /**
     * Each cargo will have a destination associated with it, \n
     * it is one of the cargo's characteristics.
     */
    private final String destination;

    /**
     * Creates a new Cargo with the given ID and destination port.
     *
     * @param id          the id of the cargo.
     * @param destination the destination associated with the cargo.
     */
    public Cargo(int id, String destination) {
        this.id = id;
        this.destination = destination;
        if (id < 0) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Retrieve the ID of this piece of cargo.
     *
     * @return the ID of this piece of cargo.
     */
    public int getId() {
        return id;
    }

    /**
     * Retrieve the destination of this piece of cargo.
     *
     * @return the destination of this piece of cargo.
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Creates a string representation of the cargo.
     * The format of the string to return is:
     * CargoClass id to destination
     * Where:
     * - CargoClass is the cargo class name.
     * - id is the id of this cargo.
     * - destination is the destination of the cargo.
     *
     * @return the human-readable string representation of this cargo.
     */
    @Override
    public String toString() {
        return "Cargo " + id + " to " + destination;
    }
}
