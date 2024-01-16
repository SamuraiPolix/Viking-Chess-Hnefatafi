import java.util.ArrayList;

public abstract class ConcretePiece implements Piece{
    protected String type;
    protected Player owner;

    public ConcretePiece(Player owner) {
        if (this.getClass().equals(Pawn.class)){
            if (owner.isPlayerOne())
                this.type = Pawn.PAWN_1;
            else
                this.type = Pawn.PAWN_2;
        }
        else
            this.type = King.KING_1;
        this.owner = owner;
    }

    @Override
    public Player getOwner() {
        return owner;
    }

    @Override
    public String getType() {
        return type;
    }

}
