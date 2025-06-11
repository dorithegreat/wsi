package com.minimax;

import java.util.ArrayList;

public class Minimax {
    // represents the current state of the game
    // encodig as follows:
    // each 2 bits represent whether the given field is empty(0), occupied by the player(1), or the opponent(2)
    // value 3 is unused and represents an error, if it were to ever occur
    // the last 14 bits are unused and may store additional meta information
    private long gameState = 0;

    private int depth;


    public int findBestMove(){
        long bestMove = 0;
        int bestValue = Integer.MIN_VALUE;
        for (Long move : getAllMoves(gameState)) {
            int value = minimax(move, 0, false);
            if (value > bestValue) {
                bestValue = value;
                bestMove = move;
            }
        }
        return getMoveFromStates(bestMove, gameState);
    }

    public static int getMoveFromStates(long prevState, long currState) {
        for (int i = 0; i < 25; i++) {
            int bitIndex = i * 2;
            long prevVal = (prevState >> bitIndex) & 3L;
            long currVal = (currState >> bitIndex) & 3L;

            if (prevVal == 0 && (currVal == 1 || currVal == 2)) {
                int row = i / 5 + 1;  // Convert to 1-based
                int col = i % 5 + 1;
                return row * 10 + col;
            }
        }

        return 0; // No valid move found or invalid input
    }

    private int minimax(long node, int depth, boolean opponent){
        int score = evaluateState(node);

        // if (depth == 0) {
        //     return score;
        // }

        if (gameFinished(node)) {
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

    private boolean gameFinished(long state){
        return true;
    }

    public void registerMove(String input){
        if (input == null || input.length() != 2) {
            throw new IllegalArgumentException("Input must be a 2-digit string.");
        }

        int row = Character.getNumericValue(input.charAt(0)) - 1;
        int col = Character.getNumericValue(input.charAt(1)) - 1;

        if (row < 0 || row >= 5 || col < 0 || col >= 5) {
            throw new IllegalArgumentException("Row and column must be between 1 and 5.");
        }

        // Calculate position in bits (2 bits per field)
        int fieldIndex = row * 5 + col;
        int bitIndex = fieldIndex * 2;

        // Clear the 2 bits at the position
        long mask = ~(3L << bitIndex);
        gameState &= mask;

        // Set bits to '10' (which is 2 in decimal)
        gameState |= (2L << bitIndex);
    }

    // * calculates the numerical value of the state's worth
    public int evaluateState(long state){
        // check for winning conditinon
        // check for losing condition
        // check how many winning possibilities there are
        return 0;
    }

    private static int longestStreak(long state, int startRow, int startCol, int dRow, int dCol) {
        int max = 0;
        int count = 0;
        int row = startRow;
        int col = startCol;

        while (row >= 0 && row < 5 && col >= 0 && col < 5) {
            if (getField(state, row, col) == 1) {
                count++;
                max = Math.max(max, count);
            } else {
                count = 0;
            }
            row += dRow;
            col += dCol;
        }

        return max;
    }

    private ArrayList<Long> getAllMoves(long state){
        ArrayList<Long> nextStates = new ArrayList<>();

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                int index = row * 5 + col;
                int bitIndex = index * 2;

                long currentValue = (state >> bitIndex) & 3L;

                // Check if the field is empty (value == 0)
                if (currentValue == 0) {
                    // Create a new state with this field set to player's pawn (1)
                    long newState = state;

                    // Clear the bits at the position
                    newState &= ~(3L << bitIndex);

                    // Set bits to '01'
                    newState |= (1L << bitIndex);

                    // Add to list
                    nextStates.add(newState);
                }
            }
        }

        return nextStates;
    }
}
