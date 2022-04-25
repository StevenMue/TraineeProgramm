package de.materna.candygame.enums;

public enum Candy {
    BONBON(0, 1, 5),
    CHOCOLATE(1, 3, 10),
    GUM(2, 1, 3),
    CHIPS(3, 3, 7),
    JELLY(4, 5, 15),
    SHOWER_ROD(5, 7, 20);

    private int nr;
    private int minPrice;
    private int maxPrice;

    Candy(int nr, int minPrice, int maxPrice) {
        this.nr = nr;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public int minPrice() {
        return this.minPrice;
    }

    public int maxPrice() {
        return this.maxPrice;
    }

    public int getNr() {
        return this.nr;
    }

    public static Candy getCandy(int id) {
        for (Candy candy : Candy.values()) {
            if (candy.getNr() == id) {
                return candy;
            }
        }
        throw new IllegalArgumentException("No Candy has this ID!");
    }
}
