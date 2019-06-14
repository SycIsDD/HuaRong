package extendForGame;

public class Step {
    private int index;

    private int x;

    private int y;

    public int getIndex() {
        return index;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Step(int index, int x, int y) {
        this.index = index;
        this.x = x;
        this.y = y;
    }
}
