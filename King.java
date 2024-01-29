public class King extends ConcretePiece {
    public static final String KING_1 = "♔";

    public King(Player owner, String id) {
        super(owner, id);
    }

    @Override
    public String getType() {
        return KING_1;
    }
}
