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

    // Compare by Squares
    Comparator<PieceStats> compareBySteps = Comparator.comparing(PieceStats::getTravelLength);

    Comparator<PieceStats> compareByIdNum = new CompareByIdNum();

    public static class CompareByIdNum implements Comparator<PieceStats> {
        public int compare(PieceStats ps1, PieceStats ps2) {
            if (Integer.parseInt(ps1.getId().substring(1)) < Integer.parseInt(ps2.getId().substring(1)))
                return -1;
            else if (Integer.parseInt(ps1.getId().substring(1)) > Integer.parseInt(ps2.getId().substring(1)))
                return 1;
            else
                return 0;
        }
    }



    // Compare by Position visits

    Comparator<PositionStats> compareByVisits = Comparator.comparing(PositionStats::getNumberOfVisits);
    Comparator<PositionStats> compareByRow = Comparator.comparing(PositionStats::getPosRow);
    Comparator<PositionStats> compareByCol = Comparator.comparing(PositionStats::getPosCol);



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
        arr.sort(compareByIdNum);
        arr.sort(compareByKills.reversed());
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
        arr.sort(compareByIdNum);
        arr.sort(compareBySteps.reversed());
        for (PieceStats pieceStats : arr) {
            if (pieceStats.getTravelLength() > 0)
                pieceStats.printSteps();
        }
    }

    public void printPositionVisits() {
        positionsList.sort(compareByCol);
        positionsList.sort(compareByRow);
        positionsList.sort(compareByVisits.reversed());
        for (PositionStats positionStats : positionsList) {
            if (positionStats.getNumberOfVisits() >= 2)
                positionStats.printVisits();
        }


    }




}
