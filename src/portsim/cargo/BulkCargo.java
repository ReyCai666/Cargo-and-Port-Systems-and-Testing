package portsim.cargo;

/**
 * Bulk cargo is a type of cargo, specifically a commodity cargo \n
 * that is transported unpacked in large quantities.
 */

public class BulkCargo extends Cargo {
    /**
     * Identifying number of the BulkCargo.
     */
    private int id;

    /**
     * Each cargo will have a destination associated with it \n
     * ,it is one of the cargo's characteristics.
     */
    private String destination;

    /**
     * A number represent the maximum weight of this type of cargo.
     */
    private final int tonnage;

    /**
     * Introduce the types of the BulkCargo.
     */
    private final BulkCargoType type;

    /**
     * Construct a new Bulk Cargo with the given ID \n
     * , destination, tonnage and type.
     *
     * @param id          the id of the bulk cargo.
     * @param destination the destination associated with the bulk cargo.
     * @param tonnage     the tonnage of the bulk cargo.
     * @param type        the type of the bulk cargo.
     */

    public BulkCargo(int id, String destination, int tonnage,
                     BulkCargoType type) {
        super(id, destination);
        this.tonnage = tonnage;
        this.type = type;
        if (tonnage < 0 || id < 0) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Returns the tonnage of this bulk cargo.
     *
     * @return the tonnage of this bulk cargo.
     */
    public int getTonnage() {
        return tonnage;
    }

    /**
     * Returns the type of this bulk cargo.
     *
     * @return the type of this bulk cargo.
     */
    public BulkCargoType getType() {
        return type;
    }

    /**
     * Creates a string representation of the bulk cargo.
     * The format of the string to return is:
     * BulkCargo id to destination [type - tonnage]
     * Where:
     * - id is the id of this cargo.
     * - destination is the destination of the cargo.
     * - type is the type of cargo.
     * - tonnage is the tonnage of the cargo.
     *
     * @return the human-readable string representation of this BulkCargo.
     */
    @Override
    public String toString() {
        return "BulkCargo " + this.getId() + " to " + this.getDestination()
                + " " + '[' + this.getType()
                + " - " + this.getTonnage() + ']';

    }
}
