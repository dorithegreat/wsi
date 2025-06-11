package com.minimax;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Hello world!
 *
 */
public class App 
{

    // default values for all the parameters
    // used mostly for testing, because debugging programs with commandline arguments is a pain
    private static String serverAddress = "127.0.0.1";
    private static int port = 1234;
    private static boolean firstPlayer = false;
    private static String username = "abcd";
    private static int maxDepth = 2;
    public static void main( String[] args ) throws IOException
    {
        if (args.length != 5) {
            System.out.println("Invalid number of arguments");
            return;
        }

        serverAddress = args[0];
        port = Integer.parseInt(args[1]);
        if (Integer.parseInt(args[2]) == 1) {
            firstPlayer = true;
        }
        else{
            firstPlayer = false;
        }
        username = args[3];
        maxDepth = Integer.parseInt(args[4]);


        
        try {
            Socket socket = new Socket(serverAddress, port);
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            System.out.println("Connected to server");


            // ? this won't work because the damn server doesn't send newlines
            // String serverMessage = reader.readLine();

            // Step 1: Wait for "700" from server
            String serverMessage = readUpTo16Bytes(in);
            if (!"700".equals(serverMessage)) {
                System.err.println("Expected '700' from server, but got: " + serverMessage);
                return;
            }
            System.out.println("Received: " + serverMessage);

            // Step 2: Send player mode and username, e.g., "1 abc"
            String credentials;
            if (firstPlayer) {
                credentials = "1";
            }
            else{
                credentials = "2";
            }
            credentials = credentials + " " + username;
            byte[] credBytes = credentials.getBytes();
            out.write(credBytes, 0, credBytes.length);

            out.flush();
            System.out.println("Sent credentials: " + credentials);


            Minimax gameState = new Minimax();
            gameState.setDepth(maxDepth);
            boolean end = false;

            while (!end) {
                String msgStr = readUpTo16Bytes(in);
                if (msgStr.isEmpty()) {
                    System.err.println("Connection closed by server.");
                    break;
                }

                int msgCode;
                try {
                    msgCode = Integer.parseInt(msgStr);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid server message: " + msgStr);
                    break;
                }

                int move = msgCode % 100;
                int code = msgCode / 100;

                if (move != 0) {
                    // ? I made registerMove() take string as input and I cannot be bothered to change it
                    gameState.registerMove(Integer.toString(move));

                }

                if (code == 0 || code == 6) {
                    // Your turn
                    int chosenMove = gameState.findBestMove();

                    String moveStr = Integer.toString(chosenMove);
                    byte[] moveBytes = moveStr.getBytes();
                    out.write(moveBytes, 0, moveBytes.length);

                    out.flush();
                    System.out.println("Sent move: " + moveStr);

                } else {
                    // End of game
                    end = true;
                    switch (code) {
                        case 1:
                            System.out.println("You won.");
                            break;
                        case 2:
                            System.out.println("You lost.");
                            break;
                        case 3:
                            System.out.println("Draw.");
                            break;
                        case 4:
                            System.out.println("You won. Opponent error.");
                            break;
                        case 5:
                            System.out.println("You lost. Your error.");
                            break;
                        default:
                            System.out.println("Unknown end code: " + code);
                    }
                }
            }

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        

        // receive messages from server


        // store game state


        // find best move based on minimax





    }

    private static String readUpTo16Bytes(InputStream in) throws IOException {
        byte[] buffer = new byte[16];
        int bytesRead = in.read(buffer);
        if (bytesRead == -1) {
            throw new IOException("Server closed connection");
        }
        return new String(buffer, 0, bytesRead).trim();
    }

}
