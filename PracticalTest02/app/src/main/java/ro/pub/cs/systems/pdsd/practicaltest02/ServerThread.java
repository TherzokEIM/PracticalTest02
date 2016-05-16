package ro.pub.cs.systems.pdsd.practicaltest02;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by therzok on 16/05/16.
 */
public class ServerThread extends Thread {

    ServerSocket serverSocket;

    public ServerThread(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            Log.d("server", "Failed to create socket" + e.toString());
        }
    }

    public void stopThread() {
        if (serverSocket != null) {
            interrupt();
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                Log.d("server", "Failde to close socket" + e.toString());
            }
        }
    }


    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Socket socket = serverSocket.accept();
                CommunicationThread communicationThread = new CommunicationThread(this, socket);
                communicationThread.start();
            }
        } catch (IOException ioException) {
            Log.d("server", "An exception has occurred: " + ioException.toString());
        }
    }

    class CommunicationThread extends Thread {
        ServerThread parent;
        Socket socket;

        public CommunicationThread(ServerThread parent, Socket socket) {
            this.parent = parent;
            this.socket = socket;
        }

        @Override
        public void run() {
            BufferedReader reader;
            PrintWriter writer;

            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            } catch (Exception e) {
                Log.d("server", "cannot instantiate buffered reader" + e.toString());
                return;
            }

            String line;
            try {
                line = reader.readLine();
                String[] elems = line.split(",");
                String result = "overflow";
                int a, b;

                a = Integer.parseInt(elems[1]);
                b = Integer.parseInt(elems[2]);

                if ("add".equals(elems[0])) {
                    if (Integer.MAX_VALUE - a > b)
                        result = String.valueOf(a + b);
                } else if ("mul".equals(elems[0])) {
                    Thread.sleep(2000);
                    if (Integer.MAX_VALUE / a > b) {
                        result = String.valueOf(a * b);
                    }
                }

                writer.println(result);
                writer.flush();
            } catch (Exception e) {
                Log.d("server", "cannot read line" + e.toString());
            }
        }
    }
}
