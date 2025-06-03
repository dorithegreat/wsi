package com.minimax;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
        Socket socket = new Socket();
        PrintWriter writer;
        BufferedReader reader;

        try {
            writer = new PrintWriter(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }


        // connect to server
        // receive messages from server
        // store game state
        // find best move based on minimax





        socket.close();
    }
}
