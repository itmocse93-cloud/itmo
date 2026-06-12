package model;

import java.io.Serializable;

/**
 * Geographic coordinates of a product.
 * Constraint: y must not exceed 20.
 */
public class Coordinates implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final int  MAX_Y            = 20;

    private float x;
    private int   y;

    public Coordinates(float x, int y) {
        this.x = x;
        setY(y);
    }

    public float getX() { return x; }
    public int   getY() { return y; }
    public void  setX(float x) { this.x = x; }

    public void setY(int y) {
        if (y > MAX_Y)
            throw new IllegalArgumentException("y cannot be greater than " + MAX_Y);
        this.y = y;
    }

    public String toCsv() {
        return x + ";" + y;
    }

    public static Coordinates fromCsv(String csv) {
        String[] p = csv.split(";");
        return new Coordinates(Float.parseFloat(p[0]), Integer.parseInt(p[1]));
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
