package com.zikram;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MancalaEnvironment implements Cloneable{
//    we assume there is only 1 pit. say each gets 6 pits
//    then array will be like this :
//    player 1 pit (6) + player 1 target (1) +
//    player 2 pit (6) + player 2 target (2)
    int[] pits;
    final int TOTAL_PITS = 6;
    final int P1_START_LOCATION = 0;
    final int P1_SCORE_LOCATION = P1_START_LOCATION + TOTAL_PITS;
    final int P2_START_LOCATION = P1_SCORE_LOCATION + 1;
    final int P2_SCORE_LOCATION = P2_START_LOCATION + TOTAL_PITS;
    final int START_GEMS = 4;
    private Map<String, Integer> p1Metrics;
    private Map<String, Integer> p2Metrics;

    public MancalaEnvironment() {
        reset();
    }
    public Map<String, Boolean> step(int pitId, boolean isP1){
        if(isP1) return playerStep1(pitId);
        return playerStep2(pitId);
    }
    public Map<String, Boolean> playerStep1(int pitId){
        Map<String, Boolean> response = getResponseMap();
        int globalId = getGlobalPosition(pitId, true);
        if(pitId >= TOTAL_PITS || pits[globalId] == 0) {
            response.put("Error", true);
            response.put("Repeat", true);
            return response;
        }
        boolean repeat = propagateGems(globalId, P2_SCORE_LOCATION);
        if(repeat) p1Metrics.compute("extra steps", (k, v) -> v + 1);
        response.put("Repeat", repeat);
        if(isNoMovesLeft(true) || isNoMovesLeft(false)){
            response.put("Terminated", true);
            distributeScores();
        }
        return response;
    }


    public int stoneInPits(boolean isP1){
        int stones = 0;
        if(isP1){
            for(int i = P1_START_LOCATION; i < P1_SCORE_LOCATION; i++) {
                stones += pits[i];
            }
        }
        else{
            for(int i = P2_START_LOCATION; i < P2_SCORE_LOCATION; i++) {
                stones += pits[i];
            }
        }
        return stones;
    }

    public Map<String, Boolean> playerStep2(int pitId){
        Map<String, Boolean> response = getResponseMap();
        int globalId = getGlobalPosition(pitId, false);
        if(pitId >= TOTAL_PITS || pits[globalId] == 0) {
            response.put("Error", true);
            response.put("Repeat", true);
            return response;
        }
        boolean repeat = propagateGems(globalId, P1_SCORE_LOCATION);
        if(repeat) p2Metrics.compute("extra steps", (k, v) -> v + 1);
        response.put("Repeat", repeat);
        if(isNoMovesLeft(true) || isNoMovesLeft(false)){
            response.put("Terminated", true);
            distributeScores();
        }
        return response;
    }

    private boolean propagateGems(int fromPitId, int ignore){
        boolean isP1 = isP1Position(fromPitId);
        int gemsInPit = pits[fromPitId];
        pits[fromPitId] = 0;
        int pitId = fromPitId;
        while(gemsInPit > 0){
            fromPitId++;
            pitId = fromPitId % pits.length;
            if(pitId == ignore) continue;
            pits[pitId]++;
            gemsInPit--;
        }
        if((isP1 && isP1ScorePosition(pitId)) || (!isP1 && isP2ScorePosition(pitId))) return true;

        boolean isFinishedInOwnPits = (isP1 && isP1Position(pitId)) || (!isP1 && isP2Position(pitId));
        if(pits[pitId] == 1 && isFinishedInOwnPits){ // last gem in empty own slot
            int relativeId = getLocalPosition(pitId);
            int toRelativeId = TOTAL_PITS - relativeId - 1;
            int toGlobalId = getGlobalPosition(toRelativeId, !isP1);

//            System.out.println("Relative ID : " + relativeId);
//            System.out.println("To Relative ID : " + toRelativeId);
//            System.out.println("To Global ID : " + toGlobalId);
            if(pits[toGlobalId] != 0) {
                int totalScore = pits[pitId] + pits[toGlobalId];
                increaseCapturedMetric(isP1, totalScore - 1);
                if(isP1Position(pitId)) increasePlayer1score(totalScore);
                else increasePlayer2score(totalScore);
                pits[pitId] = 0;
                pits[toGlobalId] = 0;
            }
        }
        return false;

    }

    private void increaseCapturedMetric(boolean isP1, int capturedStones) {
        if(isP1) p1Metrics.compute("captured stones", (k, v) -> v + capturedStones);
        else p2Metrics.compute("captured stones", (k, v) -> v + capturedStones);
    }


    private Map<String, Boolean> getResponseMap(){
        Map<String, Boolean> response = new HashMap<>();
        response.put("Error", false);
        response.put("Terminated", false);
        response.put("Repeat", false);
        return response;
    }

    private boolean isP1Position(int globalPosition) {
        return (globalPosition <= P1_SCORE_LOCATION);
    }

    private boolean isP2Position(int globalPosition) {
        return (globalPosition >= P2_START_LOCATION);
    }

    private boolean isP1ScorePosition(int globalPosition) {return globalPosition == P1_SCORE_LOCATION;}
    private boolean isP2ScorePosition(int globalPosition) {return globalPosition == P2_SCORE_LOCATION;}

    private int getLocalPosition(int globalPosition){
        if(isP1Position(globalPosition)) return globalPosition - P1_START_LOCATION;
        return globalPosition - P2_START_LOCATION;
    }
    private int getGlobalPosition(int relativePosition, boolean isP1){
        if(isP1) return relativePosition + P1_START_LOCATION;
        return relativePosition + P2_START_LOCATION;
    }

    public int getScore(boolean isP1){
        return (isP1 ? getPlayer1score() : getPlayer2score());
    }
    public int getPlayer1score() {
        return pits[P1_SCORE_LOCATION];
    }

    public int getPlayer2score() {
        return pits[P2_SCORE_LOCATION];
    }

    private void increasePlayer1score(int increment) {
        pits[P1_SCORE_LOCATION] += increment;
    }
    private void increasePlayer2score(int increment) {
        pits[P2_SCORE_LOCATION] += increment;
    }
    void reset(){
        resetMetrics();
        pits = new int[2*TOTAL_PITS + 2];
        Arrays.fill(pits, START_GEMS);
        pits[P2_SCORE_LOCATION] = 0;
        pits[P1_SCORE_LOCATION] = 0;
    }

    private void resetMetrics() {
        p1Metrics = new HashMap<>();
        p2Metrics = new HashMap<>();
//        Player1
        p1Metrics.put("extra steps", 0);
        p1Metrics.put("captured stones", 0);
//        Player2
        p2Metrics.put("extra steps", 0);
        p2Metrics.put("captured stones", 0);
    }

    void printEnv(){
        for(int i = P2_SCORE_LOCATION - 1; i >= P2_START_LOCATION; i--){
            System.out.print("   " + pits[i] );
        }
        System.out.println();
        System.out.println(getPlayer2score() + "\t\t\t\t\t\t\t" + getPlayer1score());
        for(int i = P1_START_LOCATION; i < P1_SCORE_LOCATION; i++){
            System.out.print("   " + pits[i]);
        }
        System.out.println();
    }

    void printMetrics(){
        System.out.println("----- metrics -----");
        System.out.println("\t\t\t  Player 1 \t\t Player 2");
        System.out.println("Score \t\t\t" + getPlayer1score() + "\t\t\t\t" + getPlayer2score());
        System.out.println("Extra Moves \t" + getMetrics(true).get("extra steps") + "\t\t\t\t" + getMetrics(false).get("extra steps"));
        System.out.println("Stones Captured " + getMetrics(true).get("captured stones") + "\t\t\t\t" + getMetrics(false).get("captured stones"));
    }

    private boolean isNoMovesLeft(boolean isP1){
        if(isP1){
            for(int i = P1_START_LOCATION; i < P1_SCORE_LOCATION; i++) if(pits[i] != 0) {
                return false;
            }
        }
        else{
            for(int i = P2_START_LOCATION; i < P2_SCORE_LOCATION; i++) if(pits[i] != 0) {
                return false;
            }
        }
//        System.out.println("no moves left :(");
        return true;
    }
// 2 5 2 0 1 3 1 2 5 0 0 3 3 5
    private void distributeScores(){
        int p1Score = 0;
        int p2Score = 0;
        for(int i = P1_START_LOCATION; i < P1_SCORE_LOCATION; i++) {
            p1Score += pits[i];
            pits[i] = 0;
        }
        for(int i = P2_START_LOCATION; i < P2_SCORE_LOCATION; i++) {
            p2Score += pits[i];
            pits[i] = 0;
        }
        increasePlayer1score(p1Score);
        increasePlayer2score(p2Score);
    }

    public boolean isGameOver(){
        for(int i = P1_START_LOCATION; i < P1_SCORE_LOCATION; i++) {
            if(pits[i] != 0) return false;
        }
        for(int i = P2_START_LOCATION; i < P2_SCORE_LOCATION; i++) {
            if (pits[i] != 0) return false;
        }
        return true;
    }

    public boolean isValidMove(int move, boolean isP1){
        int globalPitId = getGlobalPosition(move, isP1);
        return pits[globalPitId] != 0;
    }

    public Map<String, Integer> getMetrics(boolean isP1) {return (isP1 ? p1Metrics : p2Metrics);}

    @Override
    public MancalaEnvironment clone() {
        try {
            MancalaEnvironment clone = (MancalaEnvironment) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            clone.pits = this.pits.clone();
            clone.p1Metrics = new HashMap<>(this.p1Metrics);
            clone.p2Metrics = new HashMap<>(this.p2Metrics);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}


