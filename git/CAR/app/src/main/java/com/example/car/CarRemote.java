package com.example.car;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class CarRemote extends AppCompatActivity
{

    private Button forwardButton;
    private Button backwardsButton;
    private Button leftButton;
    private Button rightButton;
    private Button connectButton;
    private Button slowSpeedButton;
    private Button fastSpeedButton;
    private Button logoutButton;


    private final String DEVICE_ADDRESS = "00:22:06:01:40:D6"; //MAC Address of Bluetooth Module
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;

    private String command;
    private String gear;

    private boolean connectButtonClicked= false;
    private boolean slowSpeedButtonStatus = false;
    private boolean fastSpeedButtonStatus = false;

    private FirebaseAuth logoutAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //connect button variables to the buttons we created in .xml
        forwardButton = (Button) findViewById(R.id.forward);
        backwardsButton = (Button) findViewById(R.id.backwards);
        leftButton = (Button) findViewById(R.id.left);
        rightButton = (Button) findViewById(R.id.right);
        connectButton = (Button) findViewById(R.id.connect);
        slowSpeedButton = (Button) findViewById(R.id.slowSpeed);
        fastSpeedButton = (Button) findViewById(R.id.fastSpeed);
        logoutButton = (Button) findViewById(R.id.logoutButton);
        logoutAuth = FirebaseAuth.getInstance();

        //when we click the logout button we go to the login window and end car remote window
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logoutAuth.signOut();
                startActivity(new Intent(CarRemote.this, Login.class));
                Toast.makeText(CarRemote.this, "You Logged Out", Toast.LENGTH_SHORT).show();
                try {
                socket.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                finish();

            }
        });


        slowSpeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gear = "s";
                try
                {
                    if(CheckConnectivity())//we check if the app is connected to car
                    {
                        outputStream.write(gear.getBytes()); // value stored in "command" variable is sent to the bluetooth module

                        // region fade the button when is clicked and opposite
                        slowSpeedButtonStatus = true;
                        slowSpeedButton.setAlpha(0.5f);
                        slowSpeedButton.setClickable(false);

                        if(fastSpeedButtonStatus == true)
                        {
                            fastSpeedButtonStatus = false ;
                            fastSpeedButton.setAlpha(1f);
                            fastSpeedButton.setClickable(true);
                        }
                        //endregion

                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

            }
        });

        fastSpeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gear = "f";
                try {
                    if(CheckConnectivity())
                    {
                        outputStream.write(gear.getBytes()); // value stored in "command" variable is sent to the bluetooth module

                        fastSpeedButtonStatus = true;
                        fastSpeedButton.setAlpha(0.5f);
                        fastSpeedButton.setClickable(false);

                        if(slowSpeedButtonStatus == true)
                        {
                            slowSpeedButtonStatus = false ;
                            slowSpeedButton.setAlpha(1f);
                            slowSpeedButton.setClickable(true);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // region forward button is pressed
        forwardButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) // MotionEvent.ACTION_DOWN the case where you hold your finger on the button
                    {
                        command = "1";
                        try {
                            if(CheckConnectivity())
                            {
                                outputStream.write(command.getBytes()); // value stored in "command" variable is sent to the bluetooth module
                                ChooseSpeedWarning();
                            }
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (event.getAction() == MotionEvent.ACTION_UP) // MotionEvent.ACTION_UP the case where you take your finger off the button
                    {
                        command = "10";
                        try
                        {
                            if(CheckConnectivity())
                            {
                                outputStream.write(command.getBytes()); // value stored in "command" variable is sent to the bluetooth module
                            }
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    return false;
                }
        });
        //endregion

        // region backwards button is pressed
        backwardsButton.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN)// MotionEvent.ACTION_DOWN the case where you hold your finger on the button
                {

                    command = "2";

                    try
                    {
                        if(CheckConnectivity())
                        {
                            outputStream.write(command.getBytes());// value stored in "command" variable is sent to the bluetooth module
                            ChooseSpeedWarning();
                        }

                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) // MotionEvent.ACTION_UP the case where you take your finger off the button
                {
                    command = "10";
                    try
                    {
                        if(CheckConnectivity())
                        {
                            outputStream.write(command.getBytes());// value stored in "command" variable is sent to the bluetooth module
                        }
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
        //endregion

        // region left button is pressed
        leftButton.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN) // MotionEvent.ACTION_DOWN the case where you hold your finger on the button
                {

                    command = "3";

                    try
                    {
                        if(CheckConnectivity())
                        {
                            outputStream.write(command.getBytes());// value stored in "command" variable is sent to the bluetooth module
                            ChooseSpeedWarning();
                        }

                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                else if (event.getAction() == MotionEvent.ACTION_UP)// MotionEvent.ACTION_UP the case where you take your finger off the button
                {
                    command = "10";
                    try
                    {
                        if(CheckConnectivity())
                        {
                            outputStream.write(command.getBytes());// value stored in "command" variable is sent to the bluetooth module
                        }
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                }
                return false;
            }
        });
        //endregion

        //region right button is pressed
        rightButton.setOnTouchListener(new View.OnTouchListener()
        {

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN)// MotionEvent.ACTION_DOWN the case where you hold your finger on the button
                {

                    command = "4";
                    try
                    {
                        if(CheckConnectivity())
                        {
                            outputStream.write(command.getBytes());// value stored in "command" variable is sent to the bluetooth module
                            ChooseSpeedWarning();
                        }

                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) // MotionEvent.ACTION_UP the case where you take your finger off the button
                {
                    command = "10";
                    try
                    {
                        if(CheckConnectivity())
                        {
                            outputStream.write(command.getBytes());// value stored in "command" variable is sent to the bluetooth module
                        }
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
        //endregion

        // region connect button is pressed
        connectButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (BTinit())
                {
                    connectButtonClicked = true;
                    BTconnect();
                }
            }
        });
        //endregion

    }



    //region Initializes bluetooth module
    public boolean BTinit()
    {
        boolean found = false;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) //checks the compatibility of the device with the bluetooth
        {
            Toast.makeText(getApplicationContext(), "Device doesn't support bluetooth", Toast.LENGTH_SHORT).show();
        }

        if (!bluetoothAdapter.isEnabled()) //checks if the bluetooth is turned on the device, if not the app will request permissions
        {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED)
            {
                startActivityForResult(enableAdapter, 0);

                try
                {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();

        if (bondedDevices.isEmpty()) //checks if there are devices paired with the phone
        {
            Toast.makeText(CarRemote.this, "Please pair the device first" , Toast.LENGTH_SHORT ).show();
        }
        else
        {
            for (BluetoothDevice iterator : bondedDevices)//iterates through the list with devices to find the bluetooth module (HC-05)
            {
                if (iterator.getAddress().equals(DEVICE_ADDRESS))
                {
                    device = iterator;
                    found = true;
                    break;
                }
            }
        }
        return found;
    }
    //endregion

    // region Connection between devices
    public boolean BTconnect()
    {
        boolean connected = true;

        try
        {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

                socket = device.createRfcommSocketToServiceRecord(PORT_UUID); //creates a socket to handle the outgoing connection
                socket.connect();
                Toast.makeText(getApplicationContext(), "Connection to bluetooth device successful", Toast.LENGTH_LONG).show();
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
            connected = false;
        }
        if(connected)
        {
            try
            {
                outputStream = socket.getOutputStream(); //gets the output stream of the socket
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }

        return connected;


    }
    //endregion


    private void ChooseSpeedWarning()
    {

        if(slowSpeedButtonStatus == false && fastSpeedButtonStatus == false)
            Toast.makeText(getApplicationContext(), "Choose speed", Toast.LENGTH_SHORT ).show();

    }

    private boolean CheckConnectivity()
    {
        if(connectButtonClicked == false)
        {
            Toast.makeText(getApplicationContext(), "Connect to the car", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }
}





