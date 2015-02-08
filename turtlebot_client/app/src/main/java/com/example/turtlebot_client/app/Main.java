package com.example.turtlebot_client.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.BatchUpdateException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main extends Activity {

    private HashMap<Button, String> commands = new HashMap<Button, String>();
    private String HOST = "192.168.0.5";
    private Integer PORT = 50000;

    private static final String IPADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    public boolean validate(final String ip){
        Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    private void prepare_cmd_buttons(){
        commands.put((Button) findViewById(R.id.i), "i");
        commands.put((Button) findViewById(R.id.j), "j");
        commands.put((Button) findViewById(R.id.k), "k");
        commands.put((Button) findViewById(R.id.l), "l");
        commands.put((Button) findViewById(R.id.m), "m");
        commands.put((Button) findViewById(R.id.o), "o");
        commands.put((Button) findViewById(R.id.q), "q");
        commands.put((Button) findViewById(R.id.dot), ",");
        commands.put((Button) findViewById(R.id.dott), ".");
        commands.put((Button) findViewById(R.id.space_or_k), " ");
        commands.put((Button) findViewById(R.id.u), "u");
        commands.put((Button) findViewById(R.id.z), "z");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prepare_cmd_buttons();
        for (Map.Entry entry: commands.entrySet()){
            final Button button = (Button) entry.getKey();
            final String cmd = (String) entry.getValue();
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
                    if (wifi == null || !wifi.isWifiEnabled()){
                        Toast.makeText(getApplicationContext(), "TURN ON WIFI!", Toast.LENGTH_LONG).show();
                    } else {
                        if (validate(HOST)) {
                            Log.e("CMD", cmd + " !! " + button.getText().toString());
                            new Thread(new Client(HOST, PORT, cmd)).start();
                        } else {
                            Toast.makeText(getApplicationContext(), "Please set turtlebot ip in settings", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
    }

    private void showSettings(Context context){
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.settings, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText server_ip = (EditText) promptsView
                .findViewById(R.id.server_ip);
        server_ip.setText(HOST);
        final EditText port = (EditText) promptsView.findViewById(R.id.port);
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                HOST = server_ip.getText().toString();
                                PORT = Integer.parseInt(port.getText().toString());
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
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
            showSettings(Main.this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
