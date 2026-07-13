package com.example.parking_sensor_project;

/*
 * Project: Automotive Android - Proximity Sensor System
 * Author: Muhammad Umer Khan
 * Date: July 2026
 *
 * Purpose:
 * This application acts as the dashboard for a smart parking radar. It connects to a cloud-based HiveMQ broker
 * via MQTT to receive real-time distance measurements from a CC3200 microcontroller. It utilizes multi-sensory
 * feedback, translating the distance into responsive UI color changes and auditory warnings via a ToneGenerator
 * to minimize driver distraction.
 *
 * Acknowledgement/Citation:
 * The basic MQTT connection architecture and callback boilerplate code used in this file were adapted from the
 * lecture slides and course materials provided by Prof. Dr. rer. nat. Goetz Winterfeldt.
 */

import androidx.appcompat.app.AppCompatActivity;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.graphics.Color;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MainActivity extends AppCompatActivity {

    MqttClient client;
    OnMessageCallback callbackClass;
    TextView radarTextView;
    ToneGenerator toneGen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radarTextView = findViewById(R.id.radarTextView);
        toneGen = new ToneGenerator(AudioManager.STREAM_ALARM, 100);

        connect();
    }

    public void connect() {
        // Our specific HiveMQ settings
        String subTopic = "cc3200/ultrasonic";
        String broker = "tcp://broker.hivemq.com:1883";
        String clientId = "AndroidClient_" + System.currentTimeMillis();
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            client = new MqttClient(broker, clientId, persistence);

            // MQTT connection options
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            // Establish a connection
            client.connect(connOpts);
            Log.d("Debug","");

            // Set callback and subscribe
            callbackClass = new OnMessageCallback(this);
            client.setCallback(callbackClass);
            client.subscribe(subTopic);

        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            Log.d("Debug","");
        }
    }

    // This method is called by OnMessageCallback when new data arrives
    public void updateRadarScreen(String distanceString) {
        runOnUiThread(() -> {
            radarTextView.setText(String.format("%s cm", distanceString));

            try {
                // Convert the incoming string to a number so we can check it
                int distance = Integer.parseInt(distanceString.trim());

                // Change colors based on distance!
                if (distance > 50) {
                    radarTextView.setTextColor(Color.parseColor("#10B981")); // Safe Green
                } else if (distance > 15) {
                    radarTextView.setTextColor(Color.parseColor("#F59E0B")); // Warning Orange
                    toneGen.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                } else {
                    radarTextView.setTextColor(Color.parseColor("#EF4444")); // Danger Red
                    toneGen.startTone(ToneGenerator.TONE_CDMA_EMERGENCY_RINGBACK, 500);
                }
            } catch (NumberFormatException e) {
                // If the sensor sends a weird non-number string, ignore it
            }
        });
    }
}