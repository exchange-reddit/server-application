package com.omniversity.post_service.MQTT;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfig {

    @Value("${mqtt.broker}")
    private String brokerUrl;

    @Value("${mqtt.clientId}")
    private String clientId;

    //@Value("${mqtt.userName}")
    //private String userName;

    //@Value("${mqtt.password}")
    //private String password;

    public MqttConfig() {
    }

    @Bean
    public MqttConnectOptions mqttConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        // options.setUserName(userName);
        // options.setPassword(password.toCharArray());
        return options;
    }

    @Bean
    public IMqttClient mqttClient(MqttConnectOptions options) throws MqttException {
        MqttClient client = new MqttClient(brokerUrl, clientId, new MemoryPersistence());

        if (!client.isConnected()) {
            client.connect(options);
            System.out.println("MQTT client connected to broker: " + brokerUrl);
        }

        return client;
    }

}
