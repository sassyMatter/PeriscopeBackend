package com.app.services;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.CreateTopicsOptions;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
public class QueueComponent {

    private final KafkaAdmin kafkaAdmin;
    private final String topic;

    @Autowired
    public QueueComponent(KafkaAdmin kafkaAdmin,  String topic) {
        this.kafkaAdmin = kafkaAdmin;
        this.topic = topic;
    }

    public void configureQueue() {

        AdminClient adminClient = AdminClient.create(getAdminClientConfig());

        CreateTopicsOptions createTopicsOptions = new CreateTopicsOptions().validateOnly(false);

        adminClient.createTopics(Collections.singleton(new NewTopic(topic, 1, (short) 1)), createTopicsOptions);
    }

    private Map<String, Object> getAdminClientConfig() {
        return kafkaAdmin.getConfigurationProperties();
    }
    public String getTopic() {
        return topic;
    }
}
