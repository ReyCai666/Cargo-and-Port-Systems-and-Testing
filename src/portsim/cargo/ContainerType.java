package portsim.cargo;

/**
 * Represents the possible types of Containers a ship can carry.
 */
public enum ContainerType {
    /**
     * Enum constants
     *
     * @Constant OPEN_TOP - Open top shipping containers have the goods \n
     * are loaded in through the top by crane or other top loading machinery.
     * @Constant OTHER - Another form of shipping container \n
     * that does not fit into other categories.
     * @Constant REEFER - Reefer containers are big fridges that are used \n
     * to transport temperature controlled cargoes \n
     * such as fruits, meat, fish, seafood.
     * @Constant STANDARD - A large standardized shipping container, \n
     * designed and built for intermodal freight transport.
     * @Constant TANKER - Tank containers can be used for food \n
     * grade liquid storage and transport.
     */
    OPEN_TOP, OTHER, REEFER, STANDARD, TANKER
}
