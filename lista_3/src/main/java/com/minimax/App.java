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

    private static String serverAddress = "127.0.0.1";
    private static int port = 1234;
    private static boolean firstPlayer = true;
    private static String username = "abcd";
    public static void main( String[] args ) throws IOException
    {
        // if (args.length != 4) {
        //     System.out.println("Invalid number of arguments");
        //     return;
        // }

        // serverAddress = args[0];
        // port = Integer.parseInt(args[1]);
        // if (Integer.parseInt(args[2]) == 1) {
        //     firstPlayer = true;
        // }
        // else{
        //     firstPlayer = false;
        // }
        // username = args[3];

        // connect to server
        
        try {
            Socket socket = new Socket(serverAddress, port);
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            System.out.println("Connected to server");


            // ? this won't work because the damn server doesn't send newlines
            // String serverMessage = reader.readLine();

            // Step 1: Wait for "700" from server
            String serverMessage = readFixedMessage(in, 16);
            if (!"700".equals(serverMessage)) {
                System.err.println("Expected '700' from server, but got: " + serverMessage);
                return;
            }
            System.out.println("Received: " + serverMessage);

            // Step 2: Send player mode and username, e.g., "1 abc"
            String credentials = firstPlayer + " " + username;
            out.write(credentials.getBytes());
            out.flush();
            System.out.println("Sent credentials: " + credentials);

            // Step 3: Wait for "600" confirmation
            String readyMessage = readFixedMessage(in, 16);
            if (!"600".equals(readyMessage)) {
                System.err.println("Expected '600' from server, but got: " + readyMessage);
                return;
            }
            System.out.println("Received: " + readyMessage);


            Minimax gameState = new Minimax();
            boolean end = false;

            while (!end) {
                String msgStr = readFixedMessage(in, 16);
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
                    // TODO use minimax here
                    int chosenMove = gameState.findBestMove();

                    String moveStr = Integer.toString(chosenMove);
                    out.write(moveStr.getBytes());
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

        } catch (Exception e) {
            e.printStackTrace();
        }

        

        // receive messages from server


        // store game state


        // find best move based on minimax





    }

    private static String readFixedMessage(InputStream in, int length) throws IOException {
        byte[] buffer = new byte[length];
        int bytesRead = 0;
        while (bytesRead < length) {
            int result = in.read(buffer, bytesRead, length - bytesRead);
            if (result == -1) break; // EOF
            bytesRead += result;
        }
        return new String(buffer, 0, bytesRead).trim();
    }
}
