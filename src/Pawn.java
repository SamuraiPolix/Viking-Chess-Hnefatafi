public class Pawn extends ConcretePiece {

    public static final String PAWN_2 = "♟";
    public static final String PAWN_1 = "♙";
    private int eat;

    public Pawn(Player owner) {
        super(owner);
    }

    public void eat(){
        this.eat++;
    }

    public int getEats (){
        return this.eat;
    }
}
