package com.example.parking_sensor_project;

import android.util.Log;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class OnMessageCallback implements MqttCallback {
    MainActivity activity;

    // Constructor taking the MainActivity as a parameter
    public OnMessageCallback(MainActivity act){
        activity = act;
    }

    @Override
    public void connectionLost(Throwable cause) {
        // After the connection is lost, it usually reconnects here
        Log.d("Debug", "Connection lost!");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        // The messages obtained after subscribe will be executed here
        Log.d("Debug","Received message topic:" + topic);
        Log.d("Debug","Received message Qos:" + message.getQos());

        String payload = new String(message.getPayload());
        Log.d("Debug", payload);

        // Send the data back to MainActivity to update the screen
        activity.updateRadarScreen(payload);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("deliveryComplete---------" + token.isComplete());
    }
}