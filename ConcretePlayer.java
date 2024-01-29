public class ConcretePlayer implements Player{
    private int wins;
    private final boolean isPlayerOne;

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
