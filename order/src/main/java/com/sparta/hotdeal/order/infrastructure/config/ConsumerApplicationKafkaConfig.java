package com.sparta.hotdeal.order.infrastructure.config;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Slf4j
@EnableKafka
@Configuration
public class ConsumerApplicationKafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.topics.dlq}")
    private String paymentDlq;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public ConsumerApplicationKafkaConfig(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        // Kafka 브로커의 주소를 설정
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        // 메시지 키의 디시리얼라이저 클래스를 설정
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // 메시지 값의 디시리얼라이저 클래스를 설정
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // 설정된 프로퍼티로 DefaultKafkaConsumerFactory 를 생성하여 반환
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        // 컨슈머 팩토리를 리스너 컨테이너 팩토리에 설정
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(3); // 동시 처리 스레드 수 설정
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);

        // DeadLetterPublishingRecover 설정
        DeadLetterPublishingRecoverer recover = new DeadLetterPublishingRecoverer(kafkaTemplate,
                (record, exception) -> {
                    // 실패한 메시지를 DLQ 로 전송
                    return new TopicPartition(paymentDlq, record.partition());
                });

        factory.setCommonErrorHandler(new DefaultErrorHandler(recover, new FixedBackOff(0L, 0)));
        return factory;
    }
}
