public class ConcretePlayer implements Player{

    private final boolean isPlayerOne;
    private int wins;

    public ConcretePlayer(boolean isPlayerOne) {
        this.isPlayerOne = isPlayerOne;
        this.wins = 0;
    }

    @Override
    public boolean isPlayerOne() {
        return this.isPlayerOne;
    }

    @Override
    public int getWins() {
        return this.wins;
    }

    public void win() {
        this.wins++;
    }
}
