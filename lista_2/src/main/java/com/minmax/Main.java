package com.minmax;

import java.util.ArrayList;

public class Main 
{
    public static void main( String[] args )
    {
        long initialState = StateGenerator.generateInitialState();

        // System.out.println(initialState);
        // ArrayList<Long> solution = Solver.solve(initialState);

        long startTime = System.currentTimeMillis();

        // ArrayList<Long> solution = Solver.solve(0x123456789AB0DEFCL); //trivial
        // ArrayList<Long> solution = Solver.solve(0x12345678DCB09FAEL); //mid
        // ArrayList<Long> solution = Solver.solve(0x1234567EF08C9ABDL); //mid
        // ArrayList<Long> solution = Solver.solve(0x46C21DB70E9AF538L); //truly random
        // ArrayList<Long> solution = Solver.solve(0x1F5032D764CE8A9BL);
        // ArrayList<Long> solution = Solver.solve(0x28AC1470D5B6E39FL); //incredible luck
        // ArrayList<Long> solution = Solver.solve(0xBF037C2A645918EDL);

        // ArrayList<Long> solution = Solver.solve(0x758C169EA0243BDFL);
        // ArrayList<Long> solution = Solver.solve(0xCAEB986207FD4153L);
        ArrayList<Long> solution = Solver.solve(0x4CF5EABD07286913L);

        System.out.println(System.currentTimeMillis() - startTime);

        // System.out.println(solution);
        for (Long long1 : solution) {
            System.out.println(Long.toHexString(long1));
        }
        System.out.println(solution.size());
    }
}
