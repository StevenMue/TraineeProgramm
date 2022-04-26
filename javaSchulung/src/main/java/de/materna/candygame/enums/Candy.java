package de.materna.candygame.enums;

/**
 * Enum contains the kinds of candy
 * each candy has a ID, minimum price and maximum price
 */
public enum Candy {
    BONBON(0, 1, 5),
    CHOCOLATE(1, 3, 10),
    GUM(2, 1, 3),
    CHIPS(3, 3, 7),
    JELLY(4, 5, 15),
    SHOWER_ROD(5, 7, 20);

    private int ID;
    private int minPrice;
    private int maxPrice;

    Candy(int ID, int minPrice, int maxPrice) {
        this.ID = ID;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    /**
     * @return the minimum price for that candy
     */
    public int minPrice() {
        return this.minPrice;
    }
    /**
     * @return the maximum price for that candy
     */
    public int maxPrice() {
        return this.maxPrice;
    }

    /**
     * @return the ID of the candy
     */
    public int getID() {
        return this.ID;
    }

    /**
     * Return the object candy which has the given ID
     * @throws IllegalArgumentException if no candy with that ID exists
     * @param id of the candy
     * @return the object candy
     */
    public static Candy getCandy(int id) {
        for (Candy candy : Candy.values()) {
            if (candy.getID() == id) {
                return candy;
            }
        }
        throw new IllegalArgumentException("No Candy has this ID!");
    }
}
