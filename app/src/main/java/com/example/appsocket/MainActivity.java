package com.example.appsocket;

import androidx.appcompat.app.AppCompatActivity;

import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    TextView port;
    TextView ipAddress;
    TextView listMessage;

    Boolean validation = false;

    Socket client;
    ServerSocket serverSocket;
    BufferedReader bfr;
    Handler handler = new Handler();
    StringBuilder message = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        port = (TextView) findViewById(R.id.txtPort);
        ipAddress = (TextView) findViewById(R.id.txtIpAdress);
        listMessage = (TextView) findViewById(R.id.txtMessages);

        String ip = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
        ipAddress.setText(ip);

        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();
    }

    /**
     * Ce thread permet de lire les données reçu par un client.
     */
    private class SocketServerThread implements Runnable{

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(1111);
                port.setText(String.valueOf(serverSocket.getLocalPort()));

                //--- Socket lecture temps réel execution

                while(validation){
                    client = serverSocket.accept();
                    message.delete(0, message.length());
                    bfr = new BufferedReader(new InputStreamReader(client.getInputStream()));

                    //--- lecture du message reçu par le client

                    while(bfr.readLine()!=null){
                        message.append(bfr.readLine());
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listMessage.append(message + "\n");
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessageToCaisse(View view) throws IOException {
        Log.d("BTN", "Clic sur bouton");
    }
}