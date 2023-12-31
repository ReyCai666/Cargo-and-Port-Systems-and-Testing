package portsim.port;

import portsim.util.Encodable;

/**
 * A Bulk Quay is a type of quay specifically designed for the unloading of
 * Bulk Carrier vessels.
 *
 * @ass1_partial
 */
public class BulkQuay extends Quay implements Encodable {
    /**
     * The maximum weight in tonnes that the quay can handle.
     */
    private int maxTonnage;

    /**
     * Creates a new Bulk Quay with the given ID and max tonnage.
     *
     * @param id         quay ID
     * @param maxTonnage maximum tonnage the quay can handle
     * @throws IllegalArgumentException if ID or maxTonnage &lt; 0
     * @ass1
     */
    public BulkQuay(int id, int maxTonnage) throws IllegalArgumentException {
        super(id);
        if (maxTonnage < 0) {
            throw new IllegalArgumentException("maxTonnage must be greater than"
                    + " or equal to 0: " + maxTonnage);
        }
        this.maxTonnage = maxTonnage;
    }

    /**
     * Returns the maximum number of tonnes of cargo this quay
     * can handle at one time.
     *
     * @return maxTonnage
     * @ass1
     */
    public int getMaxTonnage() {
        return maxTonnage;
    }

    /**
     * Returns the human-readable string representation of this BulkQuay.
     * <p>
     * The format of the string to return is
     * <pre>BulkQuay id [Ship: imoNumber] - maxTonnage</pre>
     * Where:
     * <ul>
     * <li>{@code id} is the ID of this quay</li>
     * <li>{@code imoNumber} is the IMO number of the ship docked at this
     * quay, or {@code None} if the quay is unoccupied.</li>
     * <li>{@code maxTonnage} is the maximum weight in tonnes of this quay.</li>
     * </ul>
     * <p>
     * For example: <pre>BulkQuay 2 [Ship: 2372721] - 120</pre>
     *
     * @return string representation of this quay
     * @ass1
     */
    @Override
    public String toString() {
        return super.toString() + " - " + this.maxTonnage;
    }

    /**
     * Returns the machine-readable string representation of this BulkQuay.
     * <p>
     * The format of the string to return is
     * <pre>BulkQuay:id:imoNumber:maxTonnage</pre>
     * Where:
     * <ul>
     * <li>{@code id} is the ID of this quay</li>
     * <li>{@code imoNumber} is the IMO number of the ship docked at this
     * quay, or {@code None} if the quay is unoccupied.</li>
     * <li>{@code maxTonnage} is the maximum tonnage this quay can handle.</li>
     * </ul>
     * <p>
     * For example: <pre>BulkQuay:3:1258691:120</pre>
     *
     * @return encoded string representation of this quay
     */
    @Override
    public String encode() {
        return String.format("BulkQuay:%d:%s:%d",
                this.getId(),
                (this.getShip() != null ? this.getShip().getImoNumber()
                        : "None"),
                this.maxTonnage);
    }

    /**
     * Returns true if and only if this BulkQuay is equal
     * to the other given BulkQuay.
     * <p>
     * For two BulkQuays to be equal, they must have
     * the same ID, ship docked status
     * (must either both be empty or both be occupied)
     * and same tonnage capacity.
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
        BulkQuay bulkQuay = (BulkQuay) o;
        return this.getId() == bulkQuay.getId()
                && this.isEmpty() == bulkQuay.isEmpty();
    }

    /**
     * Returns the hash code of this BulkQuay.
     *
     * @return hash code of this quay.
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (this.getId() == 0 ? 0 : this.getId());
        return result;
    }

}
