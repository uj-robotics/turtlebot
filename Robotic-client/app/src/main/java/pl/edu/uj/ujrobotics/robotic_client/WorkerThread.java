package pl.edu.uj.ujrobotics.robotic_client;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by piotr on 28.11.14.
 */
public class WorkerThread implements Runnable {

    private Socket worker;
    private command command;

    public WorkerThread(Socket s, command command){
        this.worker = s;
        this.command = command;
    }

    @Override
    public void run() {
        try {
            DataOutputStream dout = new DataOutputStream(worker.getOutputStream());
            Log.e("WORKER", "Send + " + command.getCommand());
            dout.writeChar(command.getCommand());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
