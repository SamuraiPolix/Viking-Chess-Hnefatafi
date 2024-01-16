import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;

public class GameStats {
    private ArrayList<PieceStats> defendersList;
    private ArrayList<PieceStats> attackersList;

    private ArrayList<PositionStats> positionsList;

    private Player winner;

    public GameStats() {
        defendersList = new ArrayList<PieceStats>();
        attackersList = new ArrayList<PieceStats>();
        positionsList = new ArrayList<PositionStats>();
        this.winner = null;
    }

    public void addPieceStats(Player owner, PieceStats pieceStats) {
        if (owner.isPlayerOne()){
            defendersList.add(pieceStats);
        }
        else {
            attackersList.add(pieceStats);
        }
    }

    public void setWinner (Player winner) {
        this.winner = winner;
    }

    public void setPositionsList(PositionStats[][] arr) {
        for (int i = 0; i < arr.length; i++){
            for (int j = 0; j < arr[0].length; j++){
                if (arr[i][j].getNumberOfVisits() > 0)
                    positionsList.add(arr[i][j]);
            }
        }
    }

    Comparator<PieceStats> compareByKills = Comparator.comparing(PieceStats::getKills);
    Comparator<PieceStats> compareByMoves = Comparator.comparing(PieceStats::getNumberOfMoves);
    Comparator<PieceStats> compareBySteps = Comparator.comparing(PieceStats::getTravelLength);
    Comparator<PositionStats> compareByVisits = Comparator.comparing(PositionStats::getNumberOfVisits);

    public void printMoves(){
        ArrayList<PieceStats> winnerArr = attackersList,
                loserArr = defendersList;
        if (winner.isPlayerOne()) {
            winnerArr = defendersList;
            loserArr = attackersList;
        }
        printMoves(winnerArr);
        printMoves(loserArr);
    }

    private void printMoves (ArrayList<PieceStats> arr){
        arr.sort(compareByMoves);
        for (PieceStats pieceStats : arr) {
            if (pieceStats.getNumberOfMoves() > 1)
                pieceStats.printMoves();
        }
    }

    public void printKills(){
        ArrayList<PieceStats> winnerArr = attackersList,
                loserArr = defendersList;
        if (winner.isPlayerOne()) {
            winnerArr = defendersList;
            loserArr = attackersList;
        }

        ArrayList<PieceStats> mergedList = new ArrayList<>(winnerArr);
        mergedList.addAll(loserArr);

        printKills(mergedList);
    }

    private void printKills (ArrayList<PieceStats> arr){
        arr.sort(compareByMoves);
        for (PieceStats pieceStats : arr) {
            if (pieceStats.getKills() > 0)
                pieceStats.printKills();
        }
    }

    public void printSteps(){
        ArrayList<PieceStats> winnerArr = attackersList,
                loserArr = defendersList;
        if (winner.isPlayerOne()) {
            winnerArr = defendersList;
            loserArr = attackersList;
        }

        ArrayList<PieceStats> mergedList = new ArrayList<>(winnerArr);
        mergedList.addAll(loserArr);

        printSteps(mergedList);
    }

    private void printSteps (ArrayList<PieceStats> arr){
        arr.sort(compareBySteps);
        for (int i = arr.size() - 1; i >= 0; i--) {
            if (arr.get(i).getTravelLength() > 0)
                arr.get(i).printSteps();
        }
    }

    public void printPositionVisits() {
        positionsList.sort(compareByVisits);
        for (int i = positionsList.size() - 1; i >= 0; i--) {
            if (positionsList.get(i).getNumberOfVisits() >= 2)
                positionsList.get(i).printVisits();
        }
    }




}
