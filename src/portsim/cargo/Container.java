package portsim.cargo;

/**
 * A class represents a shipping container, \n
 * used for holding or transporting something.
 */
public class Container extends Cargo {
    /**
     * Identifying number of the container.
     */
    private int id;

    /**
     * Each cargo will have a destination associated with it, \n
     * it is one of the cargo's characteristics.
     */
    private String destination;

    /**
     * Introduces the types of the container.
     */
    private final ContainerType type;

    /**
     * Constructs a new Container of the specified ContainerType, \n
     * with the given ID and destination.
     *
     * @param id          the id of the container.
     * @param destination the destination associated with the container.
     * @param type        the different types of container.
     */
    public Container(int id, String destination, ContainerType type) {
        super(id, destination);
        this.type = type;
    }

    /**
     * Returns the type of this container.
     *
     * @return the type of this container.
     */
    public ContainerType getType() {
        return type;
    }

    /**
     * Creates the string representation of the container.
     * The format of the string to return is:
     * Container id to destination [type]
     * Where:
     * - id is the id of this cargo.
     * - destination is the destination of the cargo.
     * - type is the type of cargo.
     *
     * @return the human-readable string representation of this Container.
     */
    @Override
    public String toString() {
        return "Container" + " " + super.getId() + " " +  "to" + " "
                + super.getDestination() + " " + '[' + type + ']';
    }
}
