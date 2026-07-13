# IoT Parking Sensor Dashboard 📱🚗

This is the mobile client for an end-to-end IoT parking sensor project. It is a native Android application that connects to a cloud broker and listens for live distance measurements sent by physical hardware.

I built this to learn how to integrate IoT communication protocols (MQTT) into a mobile application and handle real-time UI updates on the main thread.

## How it works
The app connects to a public MQTT broker (HiveMQ) over raw TCP (Port 1883). It runs a background service using the Eclipse Paho library to listen to a specific data topic. When a new distance measurement arrives, the app dynamically updates the central dashboard.

<img width="1280" height="805" alt="App_Photo" src="https://github.com/user-attachments/assets/1a069d91-c8ec-437e-bce5-a6da4e5559b7" />

To simulate a real parking assistant, the UI changes color based on proximity:
* **Green:** Safe distance (> 50cm)
* **Orange:** Warning / Getting close (15cm - 50cm)
* **Red:** Danger / Stop (< 15cm)

## Tech Stack
* **Environment:** Android Studio
* **Language:** Java
* **Networking:** Eclipse Paho MQTT v3 Client (`org.eclipse.paho.client.mqttv3`)
* **UI:** Standard XML Layouts (ConstraintLayout)

## Setup & Testing
1. Clone the repository and open the project in **Android Studio**.
2. Wait for Gradle to sync and download the Paho MQTT dependencies.
3. Build and run the app on an emulator or a physical Android device.
4. **To Test Data:** You don't actually need the physical hardware to test the app UI! You can go to the [HiveMQ Web Client](http://www.hivemq.com/demos/websocket-client/), connect to `broker.hivemq.com` (Port 8884), and manually publish a number (e.g., "25") to the topic `cc3200/ultrasonic` to watch the app update instantly.
