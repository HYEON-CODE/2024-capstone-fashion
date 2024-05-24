package com.example.fashioncommuni.config.kafka;

import com.example.fashioncommuni.chat.domain.Message;

import com.example.fashioncommuni.chat.domain.Notification;
import com.google.common.collect.ImmutableMap;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

@EnableKafka // Spring Kafka 활성화 -> Kafka Listener 사용 가능
@Configuration
public class ListenerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaServer;

    @Value("${spring.kafka.consumer.topic}")
    private String topic;

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, Message> kafkaChatContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Message> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(kafkaChatConsumer());
        factory.setConcurrency(1); // 컨슈머 수를 2로 설정
        return factory;
    }

    @Bean
    public ConsumerFactory<String, Message> kafkaChatConsumer() {

        JsonDeserializer<Message> deserializer = new JsonDeserializer<>();

        deserializer.addTrustedPackages("*");

        Map<String, Object> consumerConfigurations =
                ImmutableMap.<String, Object>builder()
                        .put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer)
                        .put(ConsumerConfig.GROUP_ID_CONFIG, topic)
                        .put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class)
                        .put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer)
                        .put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")
                        .put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, "10000")
                        .put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, "200")
                        .build();

        return new DefaultKafkaConsumerFactory<>(consumerConfigurations, new StringDeserializer(), deserializer);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, Notification> kafkaNotificationContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Notification> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(kafkaNotificationConsumer());
        factory.setConcurrency(2); // 컨슈머 수를 2로 설정
        return factory;
    }

    @Bean
    public ConsumerFactory<String, Notification> kafkaNotificationConsumer() {

        JsonDeserializer<Notification> deserializer = new JsonDeserializer<>();

        deserializer.addTrustedPackages("*");

        Map<String, Object> consumerConfigurations =
                ImmutableMap.<String, Object>builder()
                        .put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer)
                        .put(ConsumerConfig.GROUP_ID_CONFIG, topic)
                        .put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class)
                        .put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer)
                        .put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")
                        .put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, "10000")
                        .put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, "200")
                        .build();

        return new DefaultKafkaConsumerFactory<>(consumerConfigurations, new StringDeserializer(), deserializer);
    }
}
