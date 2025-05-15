package com.minmax;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StateGenerator {


    public static long generateInitialState(){
        // boolean exitFlag = false;
        // long state = -1;
        // Random random = new Random();

        // // the chance of a random state being valid is 50/50 so it's unlikely this will execute more than a couple times
        // while (! exitFlag) {
        //     ArrayList<Integer> digits = new ArrayList<>();
        //     digits.add(1);
        //     for (int i = 2; i < 16; i++) {
        //         digits.add(random.nextInt(digits.size() + 1), i);
        //     }
        //     digits.add(0);
        //     if (validateState(digits)) {
        //         exitFlag = true;
        //         state = encodeState(digits);
        //     }
        // }

        // return state;

        long state = 0x123456789ABCDEF0L; // Goal state
        Random rand = new Random();
        int blankPos = 15;

        // To prevent immediate undo moves, track previous position
        int lastBlankPos = -1;

        for (int i = 0; i < 20; i++) {
            List<Integer> neighbors = new ArrayList<>();

            int row = blankPos / 4;
            int col = blankPos % 4;

            if (row > 0) neighbors.add(blankPos - 4); // Up
            if (row < 3) neighbors.add(blankPos + 4); // Down
            if (col > 0) neighbors.add(blankPos - 1); // Left
            if (col < 3) neighbors.add(blankPos + 1); // Right

            // Filter out move that would undo the previous one
            if (lastBlankPos != -1) {
                neighbors.remove((Integer) lastBlankPos);
            }

            int nextPos = neighbors.get(rand.nextInt(neighbors.size()));
            state = swapTiles(state, blankPos, nextPos);

            lastBlankPos = blankPos;
            blankPos = nextPos;
        }

        return state;
    }

    public static long encodeState(ArrayList<Integer> state){
        long output = 0L;
        for (int i = 0; i < 16; i++) {
            int value = state.get(i);
            if (value < 0 || value > 15) {
                throw new IllegalArgumentException("Tile values must be between 0 and 15.");
            }
            output |= ((long) value) << (i * 4);
        }
        return output;
    }



    private static boolean validateState(ArrayList<Integer> state){
        int inversionCount = getInversionCount(state);

        if (state.size() % 2 == 1) {
            return inversionCount % 2 == 0;
        }
        else{
            int position = findXPosition(state);
            if (position % 2 == 1) {
                return inversionCount % 2 == 0;
            }
            else{
                return inversionCount % 2 == 1;
            }
        }

    }

    private static int getInversionCount(ArrayList<Integer> digis){
        int inversionCount = 0;
        for (int i = 0; i < digis.size(); i++) {
            for (int j = i + 1; j < digis.size(); j++) {
                if (digis.get(j) != digis.get(i) && digis.get(i) > digis.get(j)) {
                    inversionCount++;
                }
            }
        }

        return inversionCount;
    }

    private static int findXPosition(ArrayList<Integer> state){
        int n = (int) Math.sqrt(state.size());

        for (int i = n - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                if (state.get(i * n + j) == 0) {
                    return n - 1;
                }
            }
        }
        return -1;
    }

    public static long[] getNeighbors(long state){
        
        long[] neighbors = new long[4];
        int count = 0;

        int emptyPos = findZero(state);
        int row = emptyPos / 4;
        int col = emptyPos % 4;

        if (row > 0){
            neighbors[count++] = swapTiles(state, emptyPos, emptyPos - 4); // Up
        }
        if (row < 3) {
            neighbors[count++] = swapTiles(state, emptyPos, emptyPos + 4); // Down
        }
        if (col > 0) {
            neighbors[count++] = swapTiles(state, emptyPos, emptyPos - 1); // Left
        }
        if (col < 3) {
            neighbors[count++] = swapTiles(state, emptyPos, emptyPos + 1); // Right
        }

        // Trim unused slots
        if (count < 4) {
            long[] result = new long[count];
            System.arraycopy(neighbors, 0, result, 0, count);
            return result;
        }

        return neighbors;
    }

    private static int findZero(long state) {
        for (int i = 0; i < 16; i++) {
            if (((state >>> (i * 4)) & 0xF) == 0) {
                return i;
            }
        }
        throw new IllegalArgumentException("Invalid puzzle state: no empty tile");
    }

    // bit twiddling trickery kindly supplied by chatgpt
    private static long swapTiles(long state, int pos1, int pos2) {
        int shift1 = pos1 * 4;
        int shift2 = pos2 * 4;
        long mask = 0xFL;

        long val1 = (state >>> shift1) & mask;
        long val2 = (state >>> shift2) & mask;

        state &= ~(mask << shift1);
        state &= ~(mask << shift2);

        state |= val1 << shift2;
        state |= val2 << shift1;

        return state;
    }

    private static int[] convertState(long state){
        String bits = Long.toBinaryString(state);

        // horrendous regex trickery
        // splits the string every 4 characters
        String[] split = bits.split("(?<=\\G....)");
        int[] numbers = new int[16];

        for (int i = 0; i < split.length; i++) {
            numbers[i] = Integer.parseInt(split[i], 2);
        }

        return numbers;
    }
}
