package battleship;

public class Ship {
    protected int cells;
    protected String name;

    public Ship(int cells, String name) {
        this.cells = cells;
        this.name = name;
    }

    public int getCells() {
        return cells;
    }

    public String getName() {
        return name;
    }
}
