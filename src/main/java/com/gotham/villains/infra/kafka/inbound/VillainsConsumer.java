package com.gotham.villains.infra.kafka.inbound;

import com.gotham.VillainEvent;
import com.gotham.villains.domain.usecase.VillainDetectedUseCase;
import com.gotham.villains.infra.database.dlt.DeadLetter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericData;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import static com.gotham.villains.infra.kafka.mapper.VillainEventMapper.toDomain;

@Service
@Slf4j
@RequiredArgsConstructor
public class VillainsConsumer {
    private final ReactiveMongoOperations operations;
    private final VillainDetectedUseCase villainDetectedUseCase;

    @RetryableTopic(
            dltTopicSuffix = "-errors-dlt",
            retryTopicSuffix = "-retry-dlt",
            attempts = "2",
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
            dltStrategy = DltStrategy.ALWAYS_RETRY_ON_ERROR)
    @KafkaListener(topics = "villains", groupId = "bat-cave")
    public void consume(ConsumerRecord<String, VillainEvent> consumerRecord) {
        final VillainEvent villainEvent = consumerRecord.value();
        var villain = toDomain(villainEvent);
        villainDetectedUseCase.process(villain)
                .subscribe();
    }

    @DltHandler
    public void handleDlt(ConsumerRecord<String, ? extends GenericData.Record> msg,
                          @Header(KafkaHeaders.ORIGINAL_TOPIC) String originalTopic,
                          @Header(KafkaHeaders.EXCEPTION_MESSAGE) String exceptionMessage,
                          @Header(KafkaHeaders.ORIGINAL_TIMESTAMP) long timestamp) {
        final var dlqId = DeadLetter.DlqId.builder()
                .originalTopic(originalTopic)
                .key(msg.key())
                .timestamp(timestamp)
                .exceptionMsg(exceptionMessage)
                .build();
        operations.save(new DeadLetter(dlqId, msg.value())).subscribe();
    }

}
