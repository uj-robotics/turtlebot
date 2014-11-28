package pl.edu.uj.ujrobotics.robotic_client;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by piotr on 25.11.14.
 */
public class Client implements Runnable{
    private String ip;
    private int port;
    private Socket clientSocket;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void bind() {
        try {
            clientSocket = new Socket(this.ip, port);
        } catch (IOException e) {
           Log.e("CLIENT", "I cannot bind socket to this IP : " + ip + ":" + port + "\n" + e.toString());
            //close();
        }
    }

    public void send(command cmd){
        try {
            DataOutputStream dout = new DataOutputStream(clientSocket.getOutputStream());
            dout.writeChar(cmd.getCommand());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Cannot close the socket");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        bind();
        send(command.LEFT_1);
    }
}
