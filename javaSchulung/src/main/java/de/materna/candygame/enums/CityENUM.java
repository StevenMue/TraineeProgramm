package de.materna.candygame.enums;

import java.awt.geom.Point2D;

public enum CityENUM {
    BRONX(0, 3, 0),
    GHETTO(1, 5, 1),
    CENTRAL(0, 0, 2),
    PARK(-1, -4, 3),
    MANHATTEN(-1, 4, 4),
    CONEY_ISLAND(3, 3, 5),
    BROOKLYN(0, -2, 6);
    private final Point2D coordinates;
    private final int ID;

    static class Coordinates extends Point2D {
        private double x;
        private double y;

        @Override
        public double getX() {
            return this.x;
        }

        @Override
        public double getY() {
            return this.y;
        }

        @Override
        public void setLocation(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    CityENUM(double x, double y, int id) {
        this.coordinates = new Coordinates();
        this.coordinates.setLocation(x, y);
        this.ID = id;
    }

    public double getDistance(CityENUM c2) {
        //distance * distance = (y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1)
        return this.coordinates.distance(c2.coordinates.getX(), c2.coordinates.getY());
    }

    public int getID() {
        return this.ID;
    }

    public static CityENUM getCity(int id) {
        for (CityENUM city : CityENUM.values()) {
            if (city.getID() == id) {
                return city;
            }
        }
        return null;
    }
}
