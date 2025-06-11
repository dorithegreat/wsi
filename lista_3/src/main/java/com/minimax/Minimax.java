package com.minimax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class Minimax {

    // due to a mistake I made during programming that was later spread by chatgpt,
    // the encoding is such that each two bits *starting from the end* represents consecutive fields on the board
    // the fields go from top left corner and then row by row
    // if the bits are 00 the field is empty, 01 is the player, 10 is the opponent, 11 is unused
    // the first 14 bits are empty
    private long gameState = 0;

    // default value, gets overridden when the app is started
    private int maxDepth = 10;

    Random random = new Random();

    //* because it's impossible to reach the same state in a different number of steps,
    //* it's possible to cache the value of each state so that it's only calculated once
    // it is however necessary to reset it every time opponent makes a move because of depth change
    //? I made it a global variable because I really don't want to pass it to each call of minimax
    private HashMap<Long, Integer> evaluated = new HashMap<>();


    public int findBestMove(){
        // long bestMove = 0;
        HashSet<Long> equallyGood = new HashSet<>();
        int bestValue = Integer.MIN_VALUE;
        evaluated = new HashMap<Long, Integer>();
        for (Long move : getAllMoves(gameState, false)) {
            int value = minimax(move, 1, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
            if (value > bestValue) {
                bestValue = value;
                equallyGood = new HashSet<>();
                equallyGood.add(move);
            }
            else if (value == bestValue) {
                equallyGood.add(move);
            }
        }
        // System.out.println("best move " + bestMove);
        long bestMove =(long) equallyGood.toArray()[random.nextInt(equallyGood.size())];

        int move = getMoveFromStates(gameState, bestMove);
        gameState = bestMove;
        return move;
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

    private int minimax(long node, int depth, int alpha, int beta, boolean opponent){
        int score = evaluateState(node);

        //? if score is MAX_VALUE then it can't be improved upon, and if it's MIN_VALUE you immediately lose anyway
        if (score == Integer.MIN_VALUE || score == Integer.MAX_VALUE) {
            return score;
        }

        if (depth >= maxDepth) {
            return score;
        }

        if (gameFinished(node)) {
            return score;
        }

        ArrayList<Long> moves = getAllMoves(node, opponent);
        if (moves.size() == 0) {
            return score;
        }

        if (! opponent) {
            int best = Integer.MIN_VALUE;
            for (Long move : moves) {
                if (evaluated.containsKey(move)) {
                    best = evaluated.get(move);
                }
                else{
                    best = Math.max(best, minimax(move, depth + 1, alpha, beta, !opponent));
                    evaluated.put(move, best);
                }
                
                if (best >= beta) {
                    break;
                }
                alpha = Math.max(alpha, best);
            }
            return best;
        }
        else{
            int best = Integer.MAX_VALUE;
            for (Long move : moves) {
                if (evaluated.containsKey(move)) {
                    best = evaluated.get(move);
                }
                else{
                    best = Math.min(best, minimax(move, depth + 1, alpha, beta, !opponent));
                }
                
                if (best <= alpha) {
                    break;
                }
                beta = Math.min(beta, best);
            }
            return best;
        }
        
    }

    private boolean gameFinished(long state){
        for (int i = 0; i < 25; i++) {
            int bitIndex = i * 2;
            long value = (state >> bitIndex) & 3L;
            if (value == 0) {
                return false; // Found an empty field
            }
        }
        return true; // All fields occupied
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

    //! THE EXPLANATION OF HOW THIS WORKS
    // The heuristic is genrerally quite simple
    // First it checks whether the player immediately loses or immediately wins in the evaluated state
    // If so, it returns appropriately either Integer.MAX_INT or Integer.MIN_INT (values chosen so that never could ever be better or worse than such states)
    // In all other cases the formula is playerFours - playerThrees - opponentFours + opponentThrees, where:
    // playerFours is all the ways that 4 in a row of player's symbols can still be put on the board
    // opponentFours is the same for the opponent
    // playerThrees is the number of ways in which a 3 (but not 4) in a row can be made by a player with just one moves (all the possibilities for immediately losing, which is quite bad)
    // opponentThrees is the same for the opponent

    // some implementation details courtesy of chatgpt ;)
    public int evaluateState(long state){
        int maxStreakPlayer = 0;
        int maxStreakOpponent = 0;

        // Check rows
        for (int row = 0; row < 5; row++) {
            maxStreakPlayer = Math.max(maxStreakPlayer, longestStreak(state, row, 0, 0, 1, 1));
            maxStreakOpponent = Math.max(maxStreakOpponent, longestStreak(state, row, 0, 0, 1, 2));
        }

        // Check columns
        for (int col = 0; col < 5; col++) {
            maxStreakPlayer = Math.max(maxStreakPlayer, longestStreak(state, 0, col, 1, 0, 1));
            maxStreakOpponent = Math.max(maxStreakOpponent, longestStreak(state, 0, col, 1, 0, 2));
        }

        // Check diagonals (\ direction)
        for (int i = 0; i <= 1; i++) {
            maxStreakPlayer = Math.max(maxStreakPlayer, longestStreak(state, i, 0, 1, 1, 1));
            maxStreakOpponent = Math.max(maxStreakOpponent, longestStreak(state, i, 0, 1, 1, 2));

            maxStreakPlayer = Math.max(maxStreakPlayer, longestStreak(state, 0, i, 1, 1, 1));
            maxStreakOpponent = Math.max(maxStreakOpponent, longestStreak(state, 0, i, 1, 1, 2));
        }

        // Check anti-diagonals (/ direction)
        for (int i = 4; i >= 3; i--) {
            maxStreakPlayer = Math.max(maxStreakPlayer, longestStreak(state, 0, i, 1, -1, 1));
            maxStreakOpponent = Math.max(maxStreakOpponent, longestStreak(state, 0, i, 1, -1, 2));

            maxStreakPlayer = Math.max(maxStreakPlayer, longestStreak(state, 5 - i - 1, 4, 1, -1, 1));
            maxStreakOpponent = Math.max(maxStreakOpponent, longestStreak(state, 5 - i - 1, 4, 1, -1, 2));
        }

        // Priority order: 4-opponent -> 4-player -> 3-player
        if (maxStreakOpponent >= 4){
            return Integer.MIN_VALUE;
            // return Integer.MAX_VALUE;
        }
        if (maxStreakPlayer >= 4){
            return Integer.MAX_VALUE;
        }
        if (maxStreakPlayer == 3){
            return Integer.MIN_VALUE;
        }
        if (maxStreakOpponent == 3) {
            return Integer.MAX_VALUE;
            // return Integer.MIN_VALUE;
        }
    

        int playerFours = 0;
        int opponentFours = 0;
        int playerThrees = 0;
        int opponentThrees = 0;

        int[][] directions = {
            {0, 1}, // horizontal
            {1, 0}, // vertical
            {1, 1}, // diagonal \
            {1, -1} // diagonal /
        };

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                for (int[] dir : directions) {
                    int dRow = dir[0], dCol = dir[1];
                    if (isInBounds(row + 4 * dRow, col + 4 * dCol)) {
                        int[] line = new int[5];
                        for (int i = 0; i < 5; i++) {
                            line[i] = getField(state, row + i * dRow, col + i * dCol);
                        }

                        int playerCount = 0;
                        int opponentCount = 0;
                        int emptyCount = 0;
                        for (int val : line) {
                            if (val == 1) playerCount++;
                            else if (val == 2) opponentCount++;
                            else emptyCount++;
                        }

                        if (opponentCount == 0) {
                            if (playerCount == 4 && emptyCount == 1) {
                                playerFours++;
                            } else if (playerCount == 3 && emptyCount == 2) {
                                // Check no 4-cell subsequence in this window already counts as a 4-in-a-row
                                boolean isAlsoFour = false;
                                for (int i = 0; i <= 1; i++) { // two 4-length subsets in 5 cells
                                    int pCount = 0, oCount = 0;
                                    for (int j = i; j < i + 4; j++) {
                                        if (line[j] == 1) pCount++;
                                        else if (line[j] == 2) oCount++;
                                    }
                                    if (oCount == 0 && pCount == 4) {
                                        isAlsoFour = true;
                                        break;
                                    }
                                }
                                if (!isAlsoFour) {
                                    playerThrees++;
                                }
                            }
                        }

                        if (playerCount == 0) {
                            if (opponentCount == 4 && emptyCount == 1) {
                                opponentFours++;
                            } else if (opponentCount == 3 && emptyCount == 2) {
                                boolean isAlsoFour = false;
                                for (int i = 0; i <= 1; i++) {
                                    int oCount = 0, pCount = 0;
                                    for (int j = i; j < i + 4; j++) {
                                        if (line[j] == 2) oCount++;
                                        else if (line[j] == 1) pCount++;
                                    }
                                    if (pCount == 0 && oCount == 4) {
                                        isAlsoFour = true;
                                        break;
                                    }
                                }
                                if (!isAlsoFour) {
                                    opponentThrees++;
                                }
                            }
                        }
                    }
                }
            }
        }
        return playerFours - playerThrees - opponentFours + opponentThrees;
    }

    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < 5 && col >= 0 && col < 5;
    }


    private static int longestStreak(long state, int startRow, int startCol, int dRow, int dCol, int targetValue) {
        int max = 0;
        int count = 0;
        int row = startRow;
        int col = startCol;

        while (row >= 0 && row < 5 && col >= 0 && col < 5) {
            if (getField(state, row, col) == targetValue) {
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


    private static int getField(long state, int row, int col) {
        int index = row * 5 + col;
        int bitIndex = index * 2;
        return (int)((state >> bitIndex) & 3L);
    }

    private ArrayList<Long> getAllMoves(long state, boolean opponent) {
        ArrayList<Long> nextStates = new ArrayList<>();
        int pieceValue;
        if (opponent) {
            pieceValue = 2;
        }
        else{
            pieceValue = 1;
        }

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                int index = row * 5 + col;
                int bitIndex = index * 2;

                long currentValue = (state >> bitIndex) & 3L;

                // Only modify empty fields
                if (currentValue == 0) {
                    long newState = state;

                    // Clear the bits at the position
                    newState &= ~(3L << bitIndex);

                    // Set bits to '01' (player) or '10' (opponent)
                    newState |= ((long) pieceValue << bitIndex);

                    nextStates.add(newState);
                }
            }
        }

        return nextStates;
    }


    public void setDepth(int depth){
        maxDepth = depth;
    }
}
