public class Pawn extends ConcretePiece {
    public static final String PAWN_2 = "♟";
    public static final String PAWN_1 = "♙";
    private int kills;

    public Pawn(Player owner, String id) {
        super(owner, id);
    }

    @Override
    public String getType() {
        if (owner.isPlayerOne())
            return PAWN_1;
        return PAWN_2;
    }

    public void addKill(){
        this.kills++;
    }

    public void removeKill() {
        this.kills--;
    }

    public int getKills (){
        return this.kills;
    }

    public void printKills() {
        System.out.println(id + ": " + this.kills + " kills");
    }
}
