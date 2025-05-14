package com.minmax;

import java.util.ArrayList;
import java.util.Random;

public class StateGenerator {


    public static long generateInitialState(){
        boolean exitFlag = false;
        long state = -1;
        Random random = new Random();

        while (! exitFlag) {
            ArrayList<Integer> digits = new ArrayList<>();
            digits.add(1);
            for (int i = 2; i < 16; i++) {
                digits.add(random.nextInt(digits.size() + 1), i);
            }
            if (validateState()) {
                exitFlag = true;
                state = encodeState(digits);
            }
        }

        return state;
    }

    public static long encodeState(ArrayList<Integer> state){
        String bits = "";
        for (Integer i : state) {
            String binary = Integer.toBinaryString(i);
            // String binary = String.format("%04d", Integer.toBinaryString(0));
            String padded = "0000".substring(binary.length()) + binary;
            bits = bits + padded;
        }
        return Integer.parseInt(bits, 2);
    }

    private static boolean validateState(){
        return false;
    }

    public static long[] getNeighbors(long state){
        
        int[] numbers = convertState(state);

        int zero = -1;
        for (int i : numbers) {
            if (i == 0) {
                zero = i;
                break;
            }
        }

        long[] neighbors = new long[4];

        if (zero >= 4) {

        }

        return neighbors;
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
