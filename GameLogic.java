import java.util.ArrayList;
import java.util.Stack;

public class GameLogic implements PlayableLogic{

    private GameStats gameStats;            // controls the stats of the game at the end
    private ConcretePlayer player1, player2;    // 2 players
    private ConcretePiece[][] boardPieces;      // saves all the pieces and their stats
    private Position[][] positions;         // saves all the positions and their stats
    private Stack<Move> moves;          // stores info about each move to be able to 'undo' move

    // Constants
    private final int BOARD_SIZE = 11;
    private final Position CORNER1 = new Position(0 ,0);
    private final Position CORNER2 = new Position(BOARD_SIZE-1 ,BOARD_SIZE-1);
    private final Position CORNER3 = new Position(0 ,BOARD_SIZE-1);
    private final Position CORNER4 = new Position(BOARD_SIZE-1 ,0);

    private boolean isSecondPlayerTurn;
    private boolean isGameFinished;

    // 0 - null, 1 - black pawn, 2 - white pawn, 3 - white king
    private Integer[][] boardTypes = {
                    {0,0,0,1,1,1,1,1,0,0,0},
                    {0,0,0,0,0,1,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0,0,0,0},
                    {1,0,0,0,0,2,0,0,0,0,1},
                    {1,0,0,0,2,2,2,0,0,0,1},
                    {1,1,0,2,2,3,2,2,0,1,1},
                    {1,0,0,0,2,2,2,0,0,0,1},
                    {1,0,0,0,0,2,0,0,0,0,1},
                    {0,0,0,0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,1,0,0,0,0,0},
                    {0,0,0,1,1,1,1,1,0,0,0} };

    public GameLogic() {
        this.isGameFinished = false;
        this.player1 = new ConcretePlayer(true);
        this.player2 = new ConcretePlayer(false);
        this.isSecondPlayerTurn = true;     // 2nd player starts
        this.boardPieces = new ConcretePiece[BOARD_SIZE][BOARD_SIZE];
        this.positions = new Position[BOARD_SIZE][BOARD_SIZE];
        this.gameStats = new GameStats();
        this.moves = new Stack<>();
        initBoardPieces();
    }

    // Initializes the board using a matrix of Integers (helps us set the board at the beginning)
    private void initBoardPieces() {
        int countAttacker = 1,
                countDefender = 1;
        String id;
        for (int j = 0; j < this.boardTypes.length; j++){
            for (int i = 0; i < this.boardTypes[0].length; i++){
                Position currPos = new Position(i, j);
                switch (boardTypes[i][j]) {
                    case 0:
                        boardPieces[i][j] = null;
                        positions[i][j] = currPos;
                        break;
                    case 1:
                        id = "A" + countAttacker;
                        boardPieces[i][j] = new Pawn(player2, id);
                        boardPieces[i][j].addMove(currPos);
                        gameStats.addPieceStats(player2, boardPieces[i][j]);
                        positions[i][j] = currPos;
                        positions[i][j].addVisit(boardPieces[i][j]);
                        countAttacker++;
                        break;
                    case 2:
                        id = "D" + countDefender;
                        boardPieces[i][j] = new Pawn(player1, id);
                        boardPieces[i][j].addMove(currPos);
                        gameStats.addPieceStats(player1, boardPieces[i][j]);
                        positions[i][j] = currPos;
                        positions[i][j].addVisit(boardPieces[i][j]);
                        countDefender++;
                        break;
                    case 3:
                        id = "K" + countDefender;
                        boardPieces[i][j] = new King(player1, id);
                        boardPieces[i][j].addMove(currPos);
                        gameStats.addPieceStats(player1, boardPieces[i][j]);
                        positions[i][j] = currPos;
                        positions[i][j].addVisit(boardPieces[i][j]);
                        countDefender++;
                        break;
                }
            }
        }
    }

    @Override
    public boolean move(Position a, Position b) {
        a = positions[a.getRow()][a.getCol()];
        b = positions[b.getRow()][b.getCol()];
        // Check all "bad" options
        if (!arePiecesAvailable(a, b) || isMoveDiagonal(a, b) || !isPathClear(a ,b) || pawnToCorner(a, b))
            return false;
        // else
        // add new move to moves stack
        this.moves.push(new Move(a, b));
        // king or "pawn to corner" - move
        if (getPieceAtPosition(a).getClass().equals(King.class) || !b.isCorner()){
            movePiece(a, b);
            // add visit to position b
            this.positions[b.getRow()][b.getCol()].addVisit(this.boardPieces[b.getRow()][b.getCol()]);
            // add move to piece
            this.boardPieces[b.getRow()][b.getCol()].addMove(b);
            // switch turns
            this.isSecondPlayerTurn = !this.isSecondPlayerTurn;
            if (getPieceAtPosition(b).getClass().equals(King.class))
                isKingCornered((King)getPieceAtPosition(b));
            else
                eatPiece(getPieceAtPosition(b), b);
        }
        return true;
    }

    // returns true if piece at a is a pawn and b is a corner
    private boolean pawnToCorner(Position a, Position b) {
        return b.isCorner() && getPieceAtPosition(a).getClass().equals(Pawn.class);
    }

    // returns true if the move is diagonal
    private boolean isMoveDiagonal(Position a, Position b) {
        return a.getCol() != b.getCol() && a.getRow() != b.getRow();
    }

    /* return true if the path is clear - no other piece between a and b
    assuming a and b are available and not diagonal */
    private boolean isPathClear(Position a, Position b) {
        // move on Y
        if (a.getRow() != b.getRow()) {
            int direction = a.getRow() > b.getRow() ? -1 : 1;
            Position currPos;
            for (int i = a.getRow()+direction; i != b.getRow() && i >= 0 && i < BOARD_SIZE; i += direction){
                currPos = new Position(i, a.getCol());
                if (getPieceAtPosition(currPos) != null)
                    return false;
            }
        }
        // move on X
        else if (a.getCol() != b.getCol()) {
            int direction = a.getCol() > b.getCol() ? -1 : 1;
            Position currPos;
            for (int i = a.getCol()+direction; i != b.getCol() && i >= 0 && i < BOARD_SIZE; i += direction){
                currPos = new Position(a.getRow(), i);
                if (getPieceAtPosition(currPos) != null)
                    return false;
            }
        }
        return true;
    }

    private boolean arePiecesAvailable(Position a, Position b) {
        // if piece is null, you cant move it, or if b isn't null than you cant move there
        if (getPieceAtPosition(a) == null || getPieceAtPosition(b) != null)
            return false;
        // if piece doesn't belong to the playing player, return false
        if ((isSecondPlayerTurn() && getPieceAtPosition(a).getOwner().isPlayerOne()) ||
                (!isSecondPlayerTurn() && !getPieceAtPosition(a).getOwner().isPlayerOne()))
            return false;
        return true;
    }

    // moves the pieces with no conditions and nothing else done, just a pure move
    private void movePiece (Position a, Position b){
        // set b
        this.boardPieces[b.getRow()][b.getCol()] = this.boardPieces[a.getRow()][a.getCol()];
        // set a
        this.boardPieces[a.getRow()][a.getCol()] = null;
    }

    // check all the enemies around _piece at newPos and eats if needed
    private void eatPiece(Piece _piece, Position newPos) {
        Pawn piece = ((Pawn) _piece);
        int i = newPos.getRow(), j = newPos.getCol();

        Position leftPos = new Position(i, j - 1);
        Position rightPos = new Position(i, j + 1);
        Position upPos = new Position(i + 1, j);
        Position downPos = new Position(i - 1, j);

        Piece left = getPieceAtPosition(leftPos);
        Piece right = getPieceAtPosition(rightPos);
        Piece up = getPieceAtPosition(upPos);
        Piece down = getPieceAtPosition(downPos);

        // check for enemies around and act accordingly
        // LEFT
        if (left != null && (j - 1) >= 0 && left.getOwner() != piece.getOwner()){
            if (left.getClass().equals(Pawn.class)) {
                // he is cornered to the left, so eat him
                if (j - 2 == -1 || leftPos.getLeft().isCorner()) {
                    ((Pawn) this.boardPieces[newPos.getRow()][newPos.getCol()]).addKill();
                    moves.peek().addKill(piece, this.boardPieces[i][j-1]);
                    this.boardPieces[i][j-1] = null;
                } else if (this.boardPieces[i][j-2] != null && !this.boardPieces[i][j-2].getClass().equals(King.class) && this.boardPieces[i][j-2].getOwner() == piece.getOwner()) {
                    ((Pawn) this.boardPieces[newPos.getRow()][newPos.getCol()]).addKill();
                    moves.peek().addKill(piece, this.boardPieces[i][j-1]);
                    this.boardPieces[i][j-1] = null;
                }
            }
            else {
                if (isKingDead(left, leftPos))
                    return;
            }
        }

        // RIGHT
        if (right != null && (j + 1) <= 10 && right.getOwner() != piece.getOwner()){
            if (right.getClass().equals(Pawn.class)) {
                // he is cornered to the right, so eat him
                if (j + 2 == BOARD_SIZE || rightPos.getRight().isCorner()) {
                    ((Pawn) this.boardPieces[newPos.getRow()][newPos.getCol()]).addKill();
                    moves.peek().addKill(piece, this.boardPieces[i][j+1]);
                    this.boardPieces[i][j+1] = null;
                } else if (this.boardPieces[i][j+2] != null && !this.boardPieces[i][j+2].getClass().equals(King.class) && this.boardPieces[i][j+2].getOwner() == piece.getOwner()) {
                    ((Pawn) this.boardPieces[newPos.getRow()][newPos.getCol()]).addKill();
                    moves.peek().addKill(piece, this.boardPieces[i][j+1]);
                    this.boardPieces[i][j+1] = null;
                }
            }
            else {
                if (isKingDead(right, rightPos))
                    return;
            }
        }

        // UP
        if (up != null && (i + 1) <= 10 && up.getOwner() != piece.getOwner()){
            if (up.getClass().equals(Pawn.class)) {
                // he is cornered above, so eat him
                if (i + 2 == BOARD_SIZE || upPos.getUp().isCorner()) {
                    ((Pawn) this.boardPieces[newPos.getRow()][newPos.getCol()]).addKill();
                    moves.peek().addKill(piece, this.boardPieces[i+1][j]);
                    this.boardPieces[i+1][j] = null;
                } else if (this.boardPieces[i+2][j] != null && !this.boardPieces[i+2][j].getClass().equals(King.class) && this.boardPieces[i+2][j].getOwner() == piece.getOwner()) {
                    ((Pawn) this.boardPieces[newPos.getRow()][newPos.getCol()]).addKill();
                    moves.peek().addKill(piece, this.boardPieces[i+1][j]);
                    this.boardPieces[i+1][j] = null;
                }
            }
            else {
                if (isKingDead(up, upPos))
                    return;
            }
        }

        // DOWN
        if (down != null && (i - 1) >= 0 && down.getOwner() != piece.getOwner()){
            if (down.getClass().equals(Pawn.class)) {
                // he is cornered below, so eat him
                if (i - 2 == -1  || downPos.getDown().isCorner()) {
                    ((Pawn) this.boardPieces[newPos.getRow()][newPos.getCol()]).addKill();
                    moves.peek().addKill(piece, this.boardPieces[i-1][j]);
                    this.boardPieces[i-1][j] = null;
                } else if (this.boardPieces[i-2][j] != null && !this.boardPieces[i-2][j].getClass().equals(King.class) && this.boardPieces[i-2][j].getOwner() == piece.getOwner()) {
                    ((Pawn) this.boardPieces[newPos.getRow()][newPos.getCol()]).addKill();
                    moves.peek().addKill(piece, this.boardPieces[i-1][j]);
                    this.boardPieces[i-1][j] = null;
                }
            }
            else {
                if (isKingDead(down, downPos))
                    return;
            }
        }
    }

    // returns true if king at pos is surrounded, if true, starts gameFinished() to end game with player2 as winner
    public boolean isKingDead(Piece king, Position pos){
        if (getPieceAtPosition(pos.getLeft()) != null &&
                getPieceAtPosition(pos.getRight()) != null &&
                getPieceAtPosition(pos.getUp()) != null &&
                getPieceAtPosition(pos.getDown()) != null &&
                getPieceAtPosition(pos.getLeft()).getOwner() != king.getOwner() &&
                getPieceAtPosition(pos.getRight()).getOwner() != king.getOwner() &&
                getPieceAtPosition(pos.getUp()).getOwner() != king.getOwner() &&
                getPieceAtPosition(pos.getDown()).getOwner() != king.getOwner()) {
            // king is dead
            this.boardPieces[pos.getRow()][pos.getCol()] = null;
            gameFinished(player2);
            return true;
        }
        return false;
    }

    // returns true if the king is on a corner and start gameFinished() with player1 as winner
    public boolean isKingCornered(King king){
        if (king.getPos().isCorner()){
            gameFinished(player1);
            return true;
        }
        return false;
    }

    @Override
    public Piece getPieceAtPosition(Position position) {
        if (position.isValid(getBoardSize()))
            return boardPieces[position.getRow()][position.getCol()];
        return null;
    }

    @Override
    public Player getFirstPlayer() {
        return this.player1;
    }

    @Override
    public Player getSecondPlayer() {
        return this.player2;
    }

    @Override
    public boolean isGameFinished() {
        return isGameFinished;
    }

    @Override
    public boolean isSecondPlayerTurn() {
        return isSecondPlayerTurn;
    }

    @Override
    public void reset() {
        resetBoard();
        this.player1 = new ConcretePlayer(true);
        this.player2 = new ConcretePlayer(false);
    }

    // finishes the game, updates wins, prints stats and resets board
    private void gameFinished(ConcretePlayer winner){
        this.isGameFinished = true;
        winner.win();
        gameStats.setWinner(winner);
        gameStats.setPositionsList(positions);
        printStats();
        resetBoard();
    }

    private void printStats() {
        gameStats.printMoves();
        System.out.println("***************************************************************************");
        gameStats.printKills();
        System.out.println("***************************************************************************");
        gameStats.printSteps();
        System.out.println("***************************************************************************");
        gameStats.printPositionVisits();
        System.out.println("***************************************************************************");
    }

    private void resetBoard() {
        this.isGameFinished = false;
        this.isSecondPlayerTurn = true;     // 2nd player starts
        this.boardPieces = new ConcretePiece[BOARD_SIZE][BOARD_SIZE];
        this.positions = new Position[BOARD_SIZE][BOARD_SIZE];
        this.moves = new Stack<>();
        initBoardPieces();
    }

    @Override
    public void undoLastMove() {
        // each time it pops a move from the stack of moves and undos it
        if (!moves.empty()){
            Move lastMove = moves.pop();
            positions[lastMove.getTo().getRow()][lastMove.getTo().getCol()].getLastVisit().removeMove();
            positions[lastMove.getTo().getRow()][lastMove.getTo().getCol()].removeVisit();
            movePiece(lastMove.getTo(), lastMove.getFrom());
            ArrayList<ConcretePiece> killed = lastMove.getKilledList();
            Pawn killer = lastMove.getKiller();
            if (!killed.isEmpty()) {
                for (ConcretePiece piece : killed){
                    killer.removeKill();
                    Position killedPos = piece.getPos();
                    this.boardPieces[killedPos.getRow()][killedPos.getCol()] = piece;
                }
            }
            this.isSecondPlayerTurn = !this.isSecondPlayerTurn;
        }
    }

    @Override
    public int getBoardSize() {
        return BOARD_SIZE;
    }
}
