package portsim.ship;


import portsim.cargo.BulkCargo;
import portsim.cargo.BulkCargoType;
import portsim.cargo.Cargo;
import portsim.port.BulkQuay;
import portsim.port.Quay;
import portsim.util.NoSuchCargoException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The class represents a ship capable of carrying bulk cargo.
 */
public class BulkCarrier extends Ship {
    /**
     * The number represent the valid IMO number length.
     */
    private static final int VALID_IMONUMBER_LENGTH = 7;

    /**
     * A unique identifying number of the bulk carrier ship \n
     * , specifically International Maritime Organisation number.
     */
    private long imoNumber;

    /**
     * The name of the bulk carrier ship.
     */
    private String name;

    /**
     * The country of origin flag of the bulk carrier ship.
     */
    private String originFlag;

    /**
     * The nautical flag of the bulk carrier ship.
     */
    private NauticalFlag flag;

    /**
     * A number representing the capacity of the bulk carrier ship.
     */
    private final int capacity;

    /**
     * The cargo list of this bulk carrier ship \n
     * , the list contains all the cargos loaded on the ship.
     */
    private List<BulkCargo> cargoList;

    /**
     * The raw type of cargo to be checked \n
     * if it can be loaded on to bulk carrier ship.
     */
    private Cargo cargo;

    /**
     * Creates a new bulk carrier with the given \n
     * IMO number, name, origin port, nautical flag and cargo capacity.
     *
     * @param imoNumber  the IMO number of the bulk carrier ship.
     * @param name       the name of the bulk carrier ship.
     * @param originFlag the country of origin flag of the bulk carrier ship.
     * @param flag       the nautical flag of the bulk carrier ship.
     * @param capacity   the capacity of the bulk carrier ship.
     */
    public BulkCarrier(long imoNumber, String name, String originFlag,
                       NauticalFlag flag, int capacity) {
        super(imoNumber, name, originFlag, flag);
        this.capacity = capacity;
        this.cargoList = new ArrayList<>();
        int imoNumberLength = String.valueOf(imoNumber).length();
        if (imoNumber < 0 || imoNumberLength < VALID_IMONUMBER_LENGTH
                || capacity < 0) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Check if this ship can dock with the specified quay.
     *
     * @param quay the given quay to be checked \n
     *             if the ship can be docked at this quay.
     * @return true if the Quay satisfies the conditions else false.
     */
    @Override
    public boolean canDock(Quay quay) {
        if (quay instanceof BulkQuay) {
            return ((BulkQuay) quay).getMaxTonnage() >= capacity;
        }
        return false;
    }

    /**
     * Checks whether the specified cargo can be loaded onto the ship.
     * The given cargo can only be loaded \n
     * if all the following conditions are true:
     * 1. The ship does not have any cargo on board
     * 2. The cargo given is a BulkCargo
     * 3. The cargo tonnage is less than or equal to the ship's tonnage capacity
     * 4. The cargo's destination is the same as the ships' origin country
     *
     * @param cargo the cargo given to be checked to see \n
     *              if it can be loaded onto the ship.
     * @return true if the Cargo satisfies the conditions else false.
     */
    @Override
    public boolean canLoad(Cargo cargo) {
        if (cargoList.isEmpty() && cargo instanceof BulkCargo) {
            return ((BulkCargo) cargo).getTonnage() <= capacity
                    && cargo.getDestination().equals(this.getOriginFlag());
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
        if (canLoad(cargo)) {
            cargoList.add((BulkCargo) cargo);
        }
    }

    /**
     * Unloads the cargo from the ship.
     * The ship's cargo should be set to null at the end of the operation.
     *
     * @return the ships' cargo.
     * @throws NoSuchCargoException - if the ship has already been unloaded.
     */
    public BulkCargo unloadCargo() throws NoSuchCargoException {
        Cargo emptyCargo = cargo;
        if (cargo == null) {
            throw (new NoSuchCargoException());
        } else {
            cargo = null;
        }
        return (BulkCargo) emptyCargo;
    }

    /**
     * Returns the current cargo onboard this vessel.
     *
     * @return the current cargo onboard this vessel.
     */
    public BulkCargo getCargo() {
        for (BulkCargo bulkCargos : cargoList) {
            return bulkCargos;
        }
        return null;
    }

    /**
     * Creates the human-readable string representation of this BulkCarrier.
     * The format of the string to return is:
     * BulkCarrier name from origin [flag] carrying cargoType
     * Where:
     * - name is the name of this ship.
     * - origin is the country of origin of this ship.
     * - flag is the nautical flag of this ship.
     * - cargoType is the type of cargo on board or \n
     * the literal String nothing if there is no cargo currently on board.
     *
     * @return the human-readable string representation of this BulkCarrier.
     */
    @Override
    public String toString() {
        if (!cargoList.isEmpty()) {
            return "BulkCarrier" + " " + name + " " + "from "
                    + originFlag + " " + '[' + flag + ']' + " " + "carrying "
                    + Arrays.toString(BulkCargoType.values());
        } else {
            return "BulkCarrier" + " " + name + " " + "from " + originFlag + " "
                    + '[' + flag + ']' + " " + "carrying "
                    + "nothing";
        }
    }
}
