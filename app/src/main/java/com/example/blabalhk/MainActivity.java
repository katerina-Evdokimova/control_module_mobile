package com.example.blabalhk;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.SplittableRandom;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView angleTextView1, angleTextView2;
    private TextView powerTextView1, powerTextView2;
    private Button button_up, button_right, button_left, button_down, button_y1, button_y2,
            button_x1, button_x2;
    // Importing also other views
    private JoystickView joystick, joystick2;
    private double LOW = -100;
    private double HIGHT = 100;
    private double LOW_2 = 0;
    private double HIGHT_2 = 255;

    private byte [] send = new byte[8];
    private boolean sendUdp;

    // TODO ПОМЕНЯТЬ!!
    private String outputIP = "localhost";
    private Integer broadcastPort = 5060;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        angleTextView1 = (TextView) findViewById(R.id.angleTextView1);
        powerTextView1 = (TextView) findViewById(R.id.powerTextView1);
        angleTextView2 = (TextView) findViewById(R.id.angleTextView2);
        powerTextView2 = (TextView) findViewById(R.id.powerTextView2);

        //Referencing also other views
        joystick = (JoystickView) findViewById(R.id.joystickView);
        joystick2 = (JoystickView) findViewById(R.id.joystickView2);

        button_up = (Button) findViewById(R.id.button);
        button_right = (Button) findViewById(R.id.button_right);
        button_left = (Button) findViewById(R.id.button_left);
        button_down = (Button) findViewById(R.id.button_down);
        button_y1 = (Button) findViewById(R.id.button_y1);
        button_y2 = (Button) findViewById(R.id.button_y2);
        button_x1 = (Button) findViewById(R.id.button_x1);
        button_x2 = (Button) findViewById(R.id.button_x2);

        //-----UDP send thread
        Thread udpSendThread = new Thread(new Runnable() {

            @Override
            public void run() {


                while (true) {

                    try {
                        Thread.sleep(100);
                    }

                    catch (InterruptedException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }

                    if (sendUdp == true) {

                        try {

                            // get server name

                            InetAddress serverAddr = InetAddress.getByName(outputIP);
                            Log.d("UDP", "C: Connecting...");

                            // create new UDP socket
                            DatagramSocket socket = new DatagramSocket();

                            // prepare data to be sent
                           // byte[] buf = udpOutputData.getBytes();

                            // create a UDP packet with data and its destination ip & port
                            DatagramPacket packet = new DatagramPacket(send, send.length, serverAddr, broadcastPort);
                            Log.d("UDP", "C: Sending: '" + new String(send) + "'");

                            // send the UDP packet
                            socket.send(packet);

                            socket.close();

                            Log.d("UDP", "C: Sent.");
                            Log.d("UDP", "C: Done.");

                            for (int i = 0; i < 8; i++){
                                send[i] = (byte) 0;
                            }
                        }

                        catch (Exception e) {

                            Log.e("UDP", "C: Error", e);

                        }

                        try {
                            Thread.sleep(100);
                        }

                        catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }


                        sendUdp = false;
                    }

                }
            }

        });

        //Event listener that always returns the variation of the angle in degrees, motion power in percentage and direction of movement
        joystick.setOnJoystickMoveListener(new JoystickView.OnJoystickMoveListener() {

            @Override
            public void onValueChanged(int angle, int power, int direction) {
                // TODO Auto-generated method stub
                double Y = Math.cos(Math.toRadians((double) angle)) * power;
                double X = Math.sin(Math.toRadians((double) angle)) * power;
                send[0] = (byte) map(X);
                send[1] = (byte) map(Y);
                angleTextView1.setText(
                        "X:" + map(X) +" Y: "+ map(Y));
                powerTextView1.setText(" " + String.valueOf(power) + "%");

                sendUdp = true;
            }
        }, JoystickView.DEFAULT_LOOP_INTERVAL);

        //Event listener that always returns the variation of the angle in degrees, motion power in percentage and direction of movement
        joystick2.setOnJoystickMoveListener(new JoystickView.OnJoystickMoveListener() {

            @Override
            public void onValueChanged(int angle, int power, int direction) {
                // TODO Auto-generated method stub
                double Y = Math.cos(Math.toRadians((double) angle)) * power;
                double X = Math.sin(Math.toRadians((double) angle)) * power;
                send[2] = (byte) map(X);
                send[3] = (byte) map(Y);
                angleTextView2.setText(
                        "X:" + map(X) +"Y: "+ map(Y));
                powerTextView2.setText(" " + String.valueOf(power) + "%");
                sendUdp = true;

            }
        }, JoystickView.DEFAULT_LOOP_INTERVAL);

        // устанавливаем один обработчик для всех кнопок
        button_up.setOnClickListener(this);
        button_down.setOnClickListener(this);
        button_left.setOnClickListener(this);
        button_right.setOnClickListener(this);

        button_x1.setOnClickListener(this);
        button_x2.setOnClickListener(this);
        button_y1.setOnClickListener(this);
        button_y2.setOnClickListener(this);

    }




    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                send[4] = 16;
                break;
            case R.id.button_down:
                send[4] += 1;
                break;
            case R.id.button_left:
                send[5] = 16;
                break;
            case R.id.button_right:
                send[5] += 1;
                break;
            case R.id.button_x1:
                send[6] = 16;
                break;
            case R.id.button_y2:
                send[7] += 1;
                break;
            case R.id.button_x2:
                send[6] += 1;
                break;
            case R.id.button_y1:
                send[7] = 16;
                break;

        }
        sendUdp = true;
    }

    public String getSend() {
        return byteArrayToHex(send);
    }

    private static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private int map(double value){


        // Положение числа в исходном отрезке, от -100 до 100
        double relative_value = (value - LOW) / (HIGHT - LOW);

        // Накладываем его на конечный отрезок
        return (int)Math.ceil(LOW_2 + (HIGHT_2 - LOW_2) * relative_value);
    }

}