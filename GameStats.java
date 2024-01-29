import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;

public class GameStats {
    // Deals with all the stats in the game - Comperators, prints, etc.
    private ArrayList<ConcretePiece> defendersList;
    private ArrayList<ConcretePiece> attackersList;
    private ArrayList<Position> positionsList;
    private Player winner;

    public GameStats() {
        defendersList = new ArrayList<>();
        attackersList = new ArrayList<>();
        positionsList = new ArrayList<>();
        this.winner = null;
    }

    public void addPieceStats(Player owner, ConcretePiece piece) {
        if (owner.isPlayerOne()){
            defendersList.add(piece);
        }
        else {
            attackersList.add(piece);
        }
    }

    public void setWinner (Player winner) {
        this.winner = winner;
    }

    // parses a matrix of positions to an arraylist, only position with at least 1 visit get to the array
    public void setPositionsList(Position[][] arr) {
        for (int i = 0; i < arr.length; i++){
            for (int j = 0; j < arr[0].length; j++){
                if (arr[i][j].getNumberOfVisits() > 0)
                    positionsList.add(arr[i][j]);
            }
        }
    }


    // Gets a list of concretePiece and returns a list of the pawns in it (without the king)
    private ArrayList<Pawn> getPawnsList(ArrayList<ConcretePiece> arr) {
        ArrayList<Pawn> newArr = new ArrayList<>();
        for (ConcretePiece piece : arr) {
            if (piece.getClass().equals(Pawn.class))
                newArr.add((Pawn) piece);
        }
        return newArr;
    }

    // Comperators
    Comparator<Pawn> compareByKills = Comparator.comparing(Pawn::getKills);
    Comparator<ConcretePiece> compareByMoves = Comparator.comparing(ConcretePiece::getNumberOfMoves);

    // Compare by Squares
    Comparator<ConcretePiece> compareBySteps = Comparator.comparing(ConcretePiece::getDistanceTraveled);

    Comparator<ConcretePiece> compareByIdNum = new CompareByIdNum();

    public static class CompareByIdNum implements Comparator<ConcretePiece> {
        public int compare(ConcretePiece ps1, ConcretePiece ps2) {
            if (Integer.parseInt(ps1.getId().substring(1)) < Integer.parseInt(ps2.getId().substring(1)))
                return -1;
            else if (Integer.parseInt(ps1.getId().substring(1)) > Integer.parseInt(ps2.getId().substring(1)))
                return 1;
            else
                return 0;
        }
    }

    // Compare by Position visits

    Comparator<Position> compareByVisits = Comparator.comparing(Position::getNumberOfVisits);
    Comparator<Position> compareByRow = Comparator.comparing(Position::getRow);
    Comparator<Position> compareByCol = Comparator.comparing(Position::getCol);

    // Prints
    public void printMoves(){
        ArrayList<ConcretePiece> winnerArr = attackersList,
                loserArr = defendersList;
        if (winner.isPlayerOne()) {
            winnerArr = defendersList;
            loserArr = attackersList;
        }
        printMoves(winnerArr);
        printMoves(loserArr);
    }

    private void printMoves (ArrayList<ConcretePiece> arr){
        arr.sort(compareByMoves);
        for (ConcretePiece piece : arr) {
            if (piece.getNumberOfMoves() > 1)
                piece.printMoves();
        }
    }

    public void printKills(){
        ArrayList<Pawn> defenderPawns = getPawnsList(defendersList),
                attackerPawns = getPawnsList(attackersList);
        ArrayList<Pawn> mergedList;
        if (winner.isPlayerOne()) {
            mergedList = new ArrayList<Pawn>(defenderPawns);
            mergedList.addAll(attackerPawns);
        }
        else {
            mergedList = new ArrayList<Pawn>(attackerPawns);
            mergedList.addAll(defenderPawns);
        }

        printKills(mergedList);
    }

    private void printKills (ArrayList<Pawn> arr){
        arr.sort(compareByIdNum);
        arr.sort(compareByKills.reversed());
        for (Pawn pawn : arr) {
            if (pawn.getKills() > 0)
                pawn.printKills();
        }
    }

    public void printSteps(){
        ArrayList<ConcretePiece> winnerArr = attackersList,
                loserArr = defendersList;
        if (winner.isPlayerOne()) {
            winnerArr = defendersList;
            loserArr = attackersList;
        }

        ArrayList<ConcretePiece> mergedList = new ArrayList<>(winnerArr);
        mergedList.addAll(loserArr);

        printSteps(mergedList);
    }

    private void printSteps (ArrayList<ConcretePiece> arr){
        arr.sort(compareByIdNum);
        arr.sort(compareBySteps.reversed());
        for (ConcretePiece pieceStats : arr) {
            if (pieceStats.getDistanceTraveled() > 0)
                pieceStats.printSteps();
        }
    }

    public void printPositionVisits() {
        positionsList.sort(compareByCol);
        positionsList.sort(compareByRow);
        positionsList.sort(compareByVisits.reversed());
        for (Position pos : positionsList) {
            if (pos.getNumberOfVisits() >= 2)
                pos.printVisits();
        }
    }
}
