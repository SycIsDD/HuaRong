package database;

public class OnlineRecord {
    private int best;

    private int sum;

    private int rank;

    public int getRank() {
        return rank;
    }

    public int getBest() {
        return best;
    }

    public int getSum() {
        return sum;
    }

    OnlineRecord(int best, int sum, int rank) {
        this.best = best;
        this.sum = sum;
        this.rank = rank;
    }
}
