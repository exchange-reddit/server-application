package com.omniversity.public_community_service.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omniversity.public_community_service.SectionPostDependency.PostSectionDependencyEntity;
import com.omniversity.public_community_service.SectionPostDependency.PostSectionDependencyRepository;
import com.omniversity.public_community_service.SectionPostDependency.dto.NewDependencyDto;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class MqttSubscriber implements ApplicationRunner {

    @Autowired
    private IMqttClient mqttClient;

    private final MqttConnectOptions mqttConnectOptions;

    @Autowired
    private PostSectionDependencyRepository postSectionDependencyRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final ExecutorService messageProcessor = Executors.newCachedThreadPool();

    // New Post Information (Inbound)
    private static final String NEW_POST_TOPIC = "new-post";

    // Deleted Post Information (Inbound)
    private static final String DELETE_POST_TOPIC = "delete-post";

    @Autowired
    public MqttSubscriber(IMqttClient mqttClient,
                          MqttConnectOptions mqttConnectOptions, PostSectionDependencyRepository repository,
                          ObjectMapper objectMapper) {
        this.mqttClient = mqttClient;
        this.mqttConnectOptions = mqttConnectOptions;
        this.postSectionDependencyRepository = repository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        mqttClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                try {
                    System.out.println("Mqtt connection established");
                    mqttClient.subscribe(NEW_POST_TOPIC, 1);
                    mqttClient.subscribe(DELETE_POST_TOPIC, 1);
                    System.out.println("Subscribed to topics: " + NEW_POST_TOPIC + " " + DELETE_POST_TOPIC);
                } catch (MqttException e) {
                    System.err.println("Subscription failed: " + e.getMessage());
                }
            }
            @Override
            public void connectionLost(Throwable cause) {
                System.out.println("MQTT connection lost: " + cause.getMessage());
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                System.out.println("Message arrived on topic " + topic);

                if (topic.equals(NEW_POST_TOPIC)) {
                    processNewPostMessage(new String(message.getPayload()));
                } else if (topic.equals(DELETE_POST_TOPIC)) {
                    processDeletePostMessage(new String(message.getPayload()));
                }
            }


            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        try {
            System.out.println("Attempting to connect to MQTT broker at " + mqttClient.getServerURI());
            mqttClient.connect(mqttConnectOptions);
        } catch (MqttException e) {
            System.err.println("Error initiating MQTT connection: " + e.getMessage());
        }
    }

    private void processNewPostMessage(String payload) {
        try {
            NewDependencyDto dependencyDto = objectMapper.readValue(payload, NewDependencyDto.class);
            PostSectionDependencyEntity entity = new PostSectionDependencyEntity();
            entity.setPostId(dependencyDto.postId());
            entity.setSectionId(dependencyDto.sectionId());
            System.out.println("Successfully processed new post");
            postSectionDependencyRepository.save(entity);
        } catch (Exception e) {
            System.err.println("Failed to parse or save new post dependency: " + e.getMessage());
        }
    }

    private void processDeletePostMessage(String payload) throws NumberFormatException {
        try {
            Long postId = Long.valueOf(payload);
            postSectionDependencyRepository.deleteRemovedPosts(postId);
            System.out.printf("Deleted dependencies with post id: %d%n", postId);
        } catch (NumberFormatException e) {
            System.err.println("A long version of id must be provided");
        } catch (Exception e) {
            System.err.println("Failed to delete or parse");
        }
    }
}
