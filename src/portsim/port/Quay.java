package portsim.port;

import portsim.ship.Ship;
import portsim.util.BadEncodingException;
import portsim.util.Encodable;
import portsim.util.NoSuchShipException;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Quay is a platform lying alongside or projecting into the water where
 * ships are moored for loading or unloading.
 *
 * @ass1_partial
 */
public abstract class Quay implements Encodable {
    /**
     * The ID of the quay.
     */
    private int id;

    /**
     * The ship currently in the Quay.
     */
    private Ship ship;

    /**
     * Creates a new Quay with the given ID, with no ship docked at the quay.
     *
     * @param id quay ID
     * @throws IllegalArgumentException if ID &lt; 0
     * @ass1
     */
    public Quay(int id) throws IllegalArgumentException {
        if (id < 0) {
            throw new IllegalArgumentException("Quay ID must be greater than"
                    + " or equal to 0: " + id);
        }
        this.id = id;
        this.ship = null;
    }

    /**
     * Get the id of this quay.
     *
     * @return quay id
     * @ass1
     */
    public int getId() {
        return id;
    }

    /**
     * Docks the given ship at the Quay so that the quay becomes occupied.
     *
     * @param ship ship to dock to the quay
     * @ass1
     */
    public void shipArrives(Ship ship) {
        this.ship = ship;
    }

    /**
     * Removes the current ship docked at the quay.
     * The current ship should be set to {@code null}.
     *
     * @return the current ship or null if quay is empty.
     * @ass1
     */
    public Ship shipDeparts() {
        Ship current = this.ship;
        this.ship = null;
        return current;
    }

    /**
     * Returns whether a ship is currently docked at this quay.
     *
     * @return true if there is no ship docked else false
     * @ass1
     */
    public boolean isEmpty() {
        return this.ship == null;
    }

    /**
     * Returns the ship currently docked at the quay.
     *
     * @return ship at quay or null if no ship is docked
     * @ass1
     */
    public Ship getShip() {
        return ship;
    }


    /**
     * Returns the human-readable string representation of this quay.
     * <p>
     * The format of the string to return is
     * <pre>QuayClass id [Ship: imoNumber]</pre>
     * Where:
     * <ul>
     *     <li>{@code id} is the ID of this quay</li>
     *     <li>{@code imoNumber} is the IMO number of the ship docked at this
     *     quay, or {@code None} if the quay is unoccupied.</li>
     * </ul>
     * <p>
     * For example: <pre>BulkQuay 1 [Ship: 2313212]</pre> or
     * <pre>ContainerQuay 3 [Ship: None]</pre>
     *
     * @return string representation of this quay
     * @ass1
     */
    @Override
    public String toString() {
        return String.format("%s %d [Ship: %s]",
                this.getClass().getSimpleName(),
                this.id,
                (this.ship != null ? this.ship.getImoNumber() : "None"));
    }

    /**
     * Returns true if and only if this Quay is equal to the other given Quay.
     * <p>
     * For two Quays to be equal, they must have the same ID and ship docked status
     * (must either both be empty or both be occupied).
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
        Quay quay = (Quay) o;
        return this.id == quay.id && this.isEmpty() == quay.isEmpty();
    }

    /**
     * Returns the hash code of this quay.
     * <p>
     * Two quays that are equal according to equals(Object) method should
     * have the same hash code.
     *
     * @return hash code of this quay.
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (this.id == 0 ? 0 : this.id);
        return result;
    }

    /**
     * Returns the machine-readable string representation of this Quay.
     * <p>
     * The format of the string to return is
     * <pre>QuayClass:id:imoNumber</pre>
     * Where:
     * <ul>
     *     <li>{@code QuayClass} is the Quay class name</li>
     *     <li>{@code id} is the ID of this quay</li>
     *     <li>{@code imoNumber} is the IMO number of the ship docked at this
     *     quay, or {@code None} if the quay is unoccupied.</li>
     * </ul>
     * <p>
     * For example: <pre>BulkQuay:3:1258691</pre> or
     * <pre>ContainerQuay:3:None</pre>
     *
     * @return string representation of this quay
     */
    @Override
    public String encode() {
        return String.format("%s:%d:%s",
                this.getClass().getSimpleName(),
                this.id,
                (this.ship != null ? this.ship.getImoNumber() : "None"));
    }

    /**
     * Reads a Quay from its encoded representation in the given string.
     * <p>
     * The format of the string should match the encoded representation of a Quay,
     * as described in encode() (and subclasses).
     * <p>
     * The encoded string is invalid if any of the following conditions are true:
     * <ul>
     *     <li>The number of colons (:) detected was more/fewer than expected.</li>
     *     <li>The quay id is not an Integer (i.e. cannot be parsed by Integer.
     *     parseInt(String)).</li>
     *     <li>The quay id is less than 0 (0).</li>
     *     <li>The quay type specified is not one of BulkQuay or ContainerQuay</li>
     *     <li>If the encoded ship is not None then the ship must exist and the
     *     imoNumber specified must be a long (i.e. can be parsed by
     *     Long.parseLong(String)).</li>
     *     <li>The quay capacity is not an integer (i.e. cannot be parsed by
     *     Integer.parseInt(String)).</li>
     *     <li>Any of the parsed values given to a subclass constructor causes
     *     an IllegalArgumentException</li>
     * </ul>
     *
     * @param string string containing the encoded Quay
     * @return decoded Quay instance
     * @throws BadEncodingException if the format of the given string is invalid
     *                              according to the rules above
     */
    public static Quay fromString(String string) throws BadEncodingException {
        // BulkQuay:3:1325
        // ContainerQuay:3:None:100
        // The number of colons (:) detected was more/fewer than expected.
        int colonCount = 0;
        for (int i = 0; i < string.length(); i++) {
            char character = string.charAt(i);
            if (character == ':') {
                colonCount++;
            }
        }
        if (colonCount != 3) {
            throw new BadEncodingException();
        }
        // The quay id is not an Integer (i.e. cannot be parsed by
        // Integer.parseInt(String)).
        int firstColonIndex = string.indexOf(":");
        String newString = string.substring(firstColonIndex + 1);
        int secondColonIndex = newString.indexOf(":") + 1;
        secondColonIndex = firstColonIndex + secondColonIndex;
        String quayId = string.substring(firstColonIndex + 1, secondColonIndex);
        try {
            Integer.parseInt(quayId);
        } catch (NumberFormatException e) {
            throw new BadEncodingException();
        }
        if (Integer.parseInt(quayId) < 0) {
            throw new BadEncodingException(); // The quay id is less than 0 (0).
        }

        // The quay type specified is not one of BulkQuay or ContainerQuay
        String quayType = string.substring(0, firstColonIndex);
        if (!(quayType.equals(BulkQuay.class.getSimpleName())
                || quayType.equals(ContainerQuay.class.getSimpleName()))) {
            throw new BadEncodingException();
        }

        ArrayList<String> quaySubclassesElements = new ArrayList<>(
                Arrays.asList(string.split(":")));
        String shipIdInQuay = quaySubclassesElements.get(2);
        if (!(shipIdInQuay.equals("None"))) {
            try {
                Ship.getShipByImoNumber(Long.parseLong(shipIdInQuay));
            } catch (NoSuchShipException e) {
                throw new BadEncodingException();
            }
            try {
                Long.parseLong(shipIdInQuay);
            } catch (NumberFormatException e) {
                throw new BadEncodingException();
            }
        }
        if (quayType.equals(BulkQuay.class.getSimpleName())) {
            try {
                Integer.parseInt(quaySubclassesElements.get(3));
            } catch (NumberFormatException e) {
                throw new BadEncodingException();
            }
            try {
                new BulkQuay(Integer.parseInt(quayId), Integer.parseInt(
                        quaySubclassesElements.get(3)));
            } catch (IllegalArgumentException e) {
                throw new BadEncodingException();
            }
            return new BulkQuay(Integer.parseInt(quayId), Integer.parseInt(
                    quaySubclassesElements.get(3)));
        }
        ArrayList<String> containerQuayElements = new ArrayList<>(
                Arrays.asList(string.split(":")));
        try {
            new ContainerQuay(Integer.parseInt(quayId), Integer.parseInt(
                    containerQuayElements.get(3)));
        } catch (IllegalArgumentException e) {
            throw new BadEncodingException();
        }
        return new ContainerQuay(Integer.parseInt(quayId), Integer.parseInt(
                containerQuayElements.get(3)));
    }
}
