package com.app.services.templates;

import lombok.Builder;
import lombok.NoArgsConstructor;
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
@Builder
@NoArgsConstructor(force = true)
public class QueueComponent {

    @Autowired
    private final KafkaAdmin kafkaAdmin;
    private final String topic;


    public QueueComponent(KafkaAdmin kafkaAdmin,  String topic) {
        this.kafkaAdmin = kafkaAdmin;
        this.topic = topic;
    }

    public void configureQueue() {

        AdminClient adminClient = AdminClient.create(getAdminClientConfig());

        CreateTopicsOptions createTopicsOptions = new CreateTopicsOptions().validateOnly(false);

        adminClient.createTopics(Collections.singleton(new NewTopic(topic, 1, (short) 1)), createTopicsOptions);
    }

    public String generateQueue(){
        return topic;
    }

    private Map<String, Object> getAdminClientConfig() {
        return kafkaAdmin.getConfigurationProperties();
    }
    public String getTopic() {
        return topic;
    }
}
