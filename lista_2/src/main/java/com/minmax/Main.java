package com.minmax;

import java.util.ArrayList;

public class Main 
{
    public static void main( String[] args )
    {
        // long initialState = StateGenerator.generateInitialState();
        ArrayList<Long> solution = Solver.solve(0x123456789AB0CDEFL);
        System.out.println(solution);
    }
}
