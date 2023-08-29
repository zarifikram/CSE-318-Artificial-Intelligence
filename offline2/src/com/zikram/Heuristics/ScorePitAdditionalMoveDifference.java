package com.zikram.Heuristics;

import com.zikram.MancalaEnvironment;

public class ScorePitAdditionalMoveDifference implements Heuristic{

    @Override
    public int calculate(MancalaEnvironment env, boolean isP1) {
        int w1 = 1;
        int score = w1 * (env.getScore(isP1) - env.getScore(!isP1));
        int w2 = 1;
        score +=  w2 * (env.stoneInPits(isP1) - env.stoneInPits(!isP1));
        int w3 = 1;
        score +=  w3 * (env.getMetrics(isP1).get("extra steps") - env.getMetrics(!isP1).get("extra steps"));
        return score;
    }
}
