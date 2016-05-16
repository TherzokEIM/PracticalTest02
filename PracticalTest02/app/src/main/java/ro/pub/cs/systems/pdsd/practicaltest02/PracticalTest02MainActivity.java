package ro.pub.cs.systems.pdsd.practicaltest02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class PracticalTest02MainActivity extends AppCompatActivity {

    ServerThread serverThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

    }

    @Override
    protected void onDestroy() {
        if (serverThread != null) {
            serverThread.stopThread();
        }

        super.onDestroy();
    }

    public void onMulClick(View view) {
        EditText editA = (EditText)findViewById(R.id.editNum1);
        EditText editB = (EditText)findViewById(R.id.editNum2);
        EditText edit = (EditText)findViewById(R.id.editPort);

        new ClientThread("mul", editA.getText().toString(), editB.getText().toString(), edit.getText().toString(), edit).start();
    }

    public void onAddClick(View view) {
        EditText editA = (EditText)findViewById(R.id.editNum1);
        EditText editB = (EditText)findViewById(R.id.editNum2);
        EditText edit = (EditText)findViewById(R.id.editPort);

        new ClientThread("add", editA.getText().toString(), editB.getText().toString(), edit.getText().toString(), edit).start();
    }

    public void onStartClick(View view) {
        EditText edit = (EditText)findViewById(R.id.editPort);
        serverThread = new ServerThread(Integer.parseInt(edit.getText().toString()));
        serverThread.start();
    }
}
