package ro.pub.cs.systems.pdsd.practicaltest02;

import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by therzok on 16/05/16.
 */
public class ClientThread extends Thread {
    static int startPort = 2324;
    static int numOpen = 0;

    Socket socket;
    String op, a, b, serverPort;
    EditText edit;

    public ClientThread(String op, String a, String b, String serverPort, EditText edit)
    {
        this.op = op;
        this.a = a;
        this.b = b;
        this.serverPort = serverPort;
        this.edit = edit;
    }

    @Override
    public void run()
    {
        BufferedReader reader;
        PrintWriter writer;

        try {
            socket = new Socket("127.0.0.1", Integer.parseInt(serverPort));
            if (socket == null) {
                Log.d("client", "failed to create socket");
                return;
            }

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

            writer.println(op + "," + a + "," + b);
            writer.flush();

            final String result = reader.readLine();

            edit.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(edit.getContext(), result, Toast.LENGTH_SHORT).show();
                }
            });

            socket.close();
        } catch (IOException e) {
            Log.d("client", "Failed to run client thread" + e.toString());
        }
    }
}
