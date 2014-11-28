package pl.edu.uj.ujrobotics.robotic_client;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;


public class MainActivity extends Activity {
    private HashMap<command, Button> buttons = new HashMap<command, Button>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttons.put(command.LEFT_1, (Button) findViewById(R.id.u));
        buttons.put(command.LEFT_2, (Button) findViewById(R.id.j));
        buttons.put(command.LEFT_3, (Button) findViewById(R.id.m));
        buttons.put(command.FORWARD_1,(Button) findViewById(R.id.i));
        buttons.put(command.FORWARD_2, (Button) findViewById(R.id.k));
        buttons.put(command.FORWARD_3, (Button) findViewById(R.id.dott));
        buttons.put(command.RIGHT_1, (Button) findViewById(R.id.o));
        buttons.put(command.RIGHT_2, (Button) findViewById(R.id.l));
        buttons.put(command.RIGHT_3, (Button) findViewById(R.id.dot));
        buttons.put(command.STOP, (Button) findViewById(R.id.space_or_k));
        buttons.put(command.FASTER, (Button) findViewById(R.id.q));
        buttons.put(command.SLOWER, (Button) findViewById(R.id.z));
        //bind buttons
        Thread t = new Thread(new Client("192.168.0.105", 50000));
        t.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
