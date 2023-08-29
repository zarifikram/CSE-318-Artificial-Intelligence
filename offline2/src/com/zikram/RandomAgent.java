package com.zikram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomAgent {
    private boolean isP1;

    public RandomAgent(boolean isP1) {
        this.isP1 = isP1;
    }
    public ReturnNode peek(MancalaEnvironment env){
        List<Integer> moves = new ArrayList<>();
        for(int i = 0; i < 6; i++) if(env.isValidMove(i, isP1)) moves.add(i);
        Collections.shuffle(moves);
        return new ReturnNode(moves.get(0), -1);
    }
}
