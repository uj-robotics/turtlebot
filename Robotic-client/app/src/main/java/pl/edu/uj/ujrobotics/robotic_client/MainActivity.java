package pl.edu.uj.ujrobotics.robotic_client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class MainActivity extends Activity {
    private ArrayList<Field> buttons = new ArrayList<Field>();
    BlockingQueue<command> queue;

    private class Field {
        Button b;
        command command;
        Field ( command c,Button b){
            this.b = b;
            this.command = c;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queue = new ArrayBlockingQueue<command>(10);
        buttons.add(new Field(command.LEFT_1, (Button) findViewById(R.id.u)));
        buttons.add(new Field(command.LEFT_2, (Button) findViewById(R.id.j)));
        buttons.add(new Field(command.LEFT_3, (Button) findViewById(R.id.m)));
        buttons.add(new Field(command.FORWARD_1, (Button) findViewById(R.id.i)));
        buttons.add(new Field(command.FORWARD_2, (Button) findViewById(R.id.k)));
        buttons.add(new Field(command.FORWARD_3, (Button) findViewById(R.id.dott)));
        buttons.add(new Field(command.RIGHT_1, (Button) findViewById(R.id.o)));
        buttons.add(new Field(command.RIGHT_2, (Button) findViewById(R.id.l)));
        buttons.add(new Field(command.RIGHT_3, (Button) findViewById(R.id.dot)));
        buttons.add(new Field(command.STOP, (Button) findViewById(R.id.space_or_k)));
        buttons.add(new Field(command.FASTER, (Button) findViewById(R.id.q)));
        buttons.add(new Field(command.SLOWER, (Button) findViewById(R.id.z)));
        //bind buttons

        for (int i = 0; i < buttons.size(); i++){
            final command tmp = buttons.get(i).command;
            buttons.get(i).b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Thread t = new Thread(new Client("192.168.0.110", 50000, tmp));
                    t.start();
                }
            });
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
