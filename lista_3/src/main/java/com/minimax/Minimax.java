package com.minimax;

import java.util.ArrayList;

public class Minimax {
    // represents the current state of the game
    // encodig as follows:
    // each 2 bits represent whether the given field is empty(0), occupied by the player(1), or the opponent(2)
    // value 3 is unused and represents an error, if it were to ever occur
    // the last 14 bits are unused and may store additional meta information
    private long gameState;

    private int depth;


    public long findBestMove(){
        return 0;
    }

    private int minimax(long node, int depth, boolean opponent){
        int score = evaluateState(node);

        if (depth == 0) {
            return score;
        }

        ArrayList<Long> moves = getAllMoves(node);
        if (moves.size() == 0) {
            return score;
        }

        if (! opponent) {
            int best = Integer.MIN_VALUE;
            for (Long move : moves) {
                best = Math.max(best, minimax(move, depth + 1, !opponent));
            }
            return best;
        }
        else{
            int best = Integer.MAX_VALUE;
            for (Long move : moves) {
                best = Math.min(best, minimax(move, depth + 1, !opponent));
            }
            return best;
        }
        
    }

    public void registerMove(int field){
        gameState = field;
    }

    // * calculates the numerical value of the state's worth
    public int evaluateState(long state){
        return 0;
    }

    private ArrayList<Long> getAllMoves(long state){
        return null;
    }
}
