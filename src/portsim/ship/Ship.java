package portsim.ship;

import portsim.port.Quay;
import portsim.cargo.Cargo;

/**
 * The class represents a ship whose movement is managed by the system.
 */
public abstract class Ship {
    /**
     * A unique identifying number of the ship, \n
     * specifically International Maritime Organisation number.
     */
    private final long imoNumber;

    /**
     * The name of the ship.
     */
    private final String name;

    /**
     * The country of origin flag of the ship.
     */
    private final String originFlag;

    /**
     * The nautical flag of the ship.
     */
    private final NauticalFlag flag;

    /**
     * Constructs a new ship with the given IMO number, \n
     * name, origin port flag and nautical flag.
     *
     * @param imoNumber  the unique imo number of the ship.
     * @param name       the name of the ship.
     * @param originFlag the country of origin flag of the ship.
     * @param flag       the nautical flag of the ship.
     * @Throws IllegalArgumentException - if imoNumber < 0 or imoNumber is \n
     * not 7 digits long (no leading zero's [0]).
     */
    public Ship(long imoNumber, String name, String originFlag,
                NauticalFlag flag) {
        this.imoNumber = imoNumber;
        this.name = name;
        this.originFlag = originFlag;
        this.flag = flag;
        int imoNumberLength = String.valueOf(imoNumber).length();
        if (imoNumber < 0 || imoNumberLength < 7) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Check if this ship can dock with the specified quay according \n
     * to the conditions determined by the ships type.
     *
     * @param quay the given quay to be checked if the \n
     *             ship can be docked at this quay.
     * @return true if the Quay satisfies the conditions else false.
     */
    public abstract boolean canDock(Quay quay);

    /**
     * Checks if the specified cargo can be loaded onto \n
     * the ship according to the conditions \n
     * determined by the ships type.
     *
     * @param cargo the cargo given to be checked to see \n
     *              if it can be loaded onto the ship.
     * @return true if the Cargo satisfies the conditions else false.
     */
    public abstract boolean canLoad(Cargo cargo);

    /**
     * Loads the specified cargo onto the ship.
     *
     * @param cargo the cargo given to be loaded onto the ship.
     */
    public abstract void loadCargo(Cargo cargo);

    /**
     * Returns this ship's name.
     *
     * @return this ship's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns this ship's IMO number.
     *
     * @return this ship's IMO number.
     */
    public long getImoNumber() {
        return imoNumber;
    }

    /**
     * Returns this ship's flag denoting its origin.
     *
     * @return this ship's flag denoting its origin.
     */
    public String getOriginFlag() {
        return originFlag;
    }

    /**
     * Returns the nautical flag the ship is flying.
     *
     * @return the nautical flag the ship is flying.
     */
    public NauticalFlag getFlag() {
        return flag;
    }

    /**
     * Creates the human-readable string representation of this Ship.
     *
     * @return the human-readable string representation of this Ship.
     */
    @Override
    public String toString() {
        return "Ship " + name + " from " + originFlag + " " + '[' + flag + ']';
    }
}
