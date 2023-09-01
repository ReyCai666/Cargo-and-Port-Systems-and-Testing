package portsim.ship;


import portsim.cargo.Cargo;
import portsim.cargo.Container;
import portsim.port.ContainerQuay;
import portsim.port.Quay;
import portsim.util.NoSuchCargoException;

import java.util.ArrayList;
import java.util.List;

/**
 * A class represents a ship capable of carrying shipping containers.
 */
public class ContainerShip extends Ship {
    /**
     * The number represent the valid IMO number length.
     */
    private static final int VALID_IMONUMBER_LENGTH = 7;

    /**
     * A unique identifying number of the container ship, \n
     * specifically International Maritime Organisation number.
     */
    private long imoNumber;

    /**
     * The name of the container ship.
     */
    private String name;

    /**
     * The country of origin flag of the ship.
     */
    private String originFlag;

    /**
     * The nautical flag of the container ship.
     */
    private NauticalFlag flag;

    /**
     * The capacity of the container ship.
     */
    private final int capacity;

    /**
     * A list representing the container ship's ship board, \n
     * it will be used to determine what cargos are
     * loaded onto the container ship, \n
     * and whether the ship board is empty or not.
     */
    private List<Container> shipBoard;

    /**
     * Constructs a new container ship with the given IMO number, \n
     * name and origin port,
     * nautical flag and cargo capacity.
     *
     * @param imoNumber  the IMO number of the container ship.
     * @param name       the name of the container ship.
     * @param originFlag the country of origin flag of the container ship.
     * @param flag       the nautical flag of the container ship.
     * @param capacity   the capacity of the container ship.
     */
    public ContainerShip(long imoNumber, String name, String originFlag,
                         NauticalFlag flag, int capacity) {
        super(imoNumber, name, originFlag, flag);
        this.capacity = capacity;
        shipBoard = new ArrayList<>();
        int imoNumberLength = String.valueOf(imoNumber).length();
        if (imoNumber < 0 || imoNumberLength < VALID_IMONUMBER_LENGTH
                || capacity < 0) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Checks if this ship can dock with the specified quay.
     * The conditions for a compatible quay are:
     * 1. Quay must be a ContainerQuay.
     * 2. The quays maximum number of containers must \n
     * be ≥ the number of containers on board.
     *
     * @param quay - the given quay to be checked if \n
     *             the ship can be docked at this quay.
     * @return true if the Quay satisfies the conditions else false.
     */
    @Override
    public boolean canDock(Quay quay) {
        if (quay instanceof ContainerQuay) {
            return ((ContainerQuay) quay).getMaxContainers() >= capacity;
        }
        return false;
    }

    /**
     * Checks whether the specified cargo can be loaded onto the ship.
     * The given cargo can only be loaded if \n
     * all the following conditions are true:
     * 1. The cargo given is a Container.
     * 2. The current number of containers on board \n
     * is less than the container capacity.
     * 3. The cargo's destination is the same as the ships' origin country.
     *
     * @param cargo - cargo to be loaded.
     * @return true if the Cargo satisfies the conditions else false.
     */
    @Override
    public boolean canLoad(Cargo cargo) {
        if (cargo instanceof Container) {
            return shipBoard.size() <= capacity
                    && cargo.getDestination().equals(originFlag);
        }
        return false;
    }

    /**
     * Loads the specified cargo onto the ship.
     *
     * @param cargo the cargo given to be loaded onto the ship.
     */
    @Override
    public void loadCargo(Cargo cargo) {
        shipBoard.add((Container) cargo);
    }

    /**
     * Unloads the cargo from the ship.
     * The ship's cargo should be set to an empty list.
     *
     * @return the ship's cargo before it was unloaded.
     * @throws NoSuchCargoException - if the ship has already \n
     * been unloaded (i.e. the ship has no cargo onboard).
     */
    public List<Container> unloadCargo() throws NoSuchCargoException {
        if (shipBoard.isEmpty()) {
            throw (new NoSuchCargoException());
        } else {
            shipBoard = new ArrayList<>();
        }
        return shipBoard;
    }

    /**
     * Returns the current cargo onboard this vessel.
     * Adding or removing elements from the returned \n
     * list should not affect the original list.
     *
     * @return containers on the vessel.
     */
    public List<Container> getCargo() {
        return new ArrayList<>(shipBoard);
    }

    /**
     * Returns the human-readable string representation of this ContainerShip.
     * The format of the string to return is:
     * ContainerShip name from origin [flag] carrying num containers
     * Where:
     * - name is the name of this ship
     * - origin is the country of origin of this ship
     * - flag is the nautical flag of this ship
     * - num is the number of containers on board
     *
     * @return string representation of this ContainerShip.
     */
    @Override
    public String toString() {
        return "ContainerShip" + " " + name + " from " + originFlag
                + " [" + flag + " ]" + " carrying "
                + shipBoard.size() + " containers";
    }
}
