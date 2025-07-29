package com.omniversity.public_community_service.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfig {

    @Value("${mqtt.broker")
    private String brokerUrl;

    @Value("${mqtt.clientId")
    private String clientId;

    @Value("${mqtt.userName")
    private String userName;

    @Value("${mqtt.password")
    private String password;

    @Bean
    public MqttClient mqttClient() throws MqttException {
        MqttClient client = new MqttClient(brokerUrl, clientId, new MemoryPersistence());

        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        options.setUserName(userName);
        options.setPassword(password.toCharArray());

        System.out.println("Connecting to MQTT broker at " + brokerUrl);
        client.connect(options);
        System.out.println("MQTT client connected");

        return client;

    }
}
