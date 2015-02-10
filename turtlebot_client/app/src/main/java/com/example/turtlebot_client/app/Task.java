package com.example.turtlebot_client.app;

import android.util.Log;

public class Task implements Runnable {

    Thread backgroundThread;
    String HOST;
    Integer PORT;
    String cmd;
    Task(String HOST, Integer PORT, String cmd){
        this.HOST = HOST;
        this.PORT = PORT;
        this.cmd = cmd;
    }


    public void start() {
        if( backgroundThread == null ) {
            backgroundThread = new Thread( this );
            backgroundThread.start();
        }
    }

    public void stop() {
        if( backgroundThread != null ) {
            backgroundThread.interrupt();
        }
    }

    public void run() {
        try {
            Log.i("Thread","Thread starting.");
            while( !backgroundThread.interrupted() ) {
                new Thread(new Client(HOST, PORT, cmd)).start();
            }
            Log.i("Thread","Thread stopping.");
        } catch( Exception ex ) {
            // important you respond to the InterruptedException and stop processing
            // when its thrown!  Notice this is outside the while loop.
            Log.i("Thread","Thread shutting down as it was requested to stop.");
        } finally {
            backgroundThread = null;
        }
    }
}