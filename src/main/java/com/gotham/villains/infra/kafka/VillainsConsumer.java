package com.gotham.villains.infra.kafka;

import com.gotham.v1.VillainEvent;
import com.gotham.villains.domain.usecase.VillainDetectedUseCase;
import com.gotham.villains.domain.vilain.model.Villain;
import com.gotham.villains.domain.vilain.model.VillainStatus;
import com.gotham.villains.infra.database.dlq.Dlq;
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

@Service
@Slf4j
@RequiredArgsConstructor
public class VillainsConsumer {
    private final VillainDetectedUseCase villainDetectedUseCase;
    private final ReactiveMongoOperations operations;

    @RetryableTopic(
            dltTopicSuffix = "-errors-dlt",
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_DELAY_VALUE,
            dltStrategy = DltStrategy.ALWAYS_RETRY_ON_ERROR)
    @KafkaListener(topics = "villains", groupId = "bat-cave")
    public void consume(ConsumerRecord<String, VillainEvent> consumerRecord) {
        final VillainEvent villainEvent = consumerRecord.value();
        final String nickName = String.valueOf(villainEvent.getNickname());
        final String fullName = String.valueOf(villainEvent.getFullName());
        final String status = villainEvent.getStatus().name();
        villainDetectedUseCase.process(new Villain(nickName, fullName, VillainStatus.valueOf(status))).subscribe();
    }

    @DltHandler
    public void listenDlt(ConsumerRecord<String, ? extends GenericData.Record> msg,
                          @Header(KafkaHeaders.ORIGINAL_TOPIC) String originalTopic,
                          @Header(KafkaHeaders.EXCEPTION_MESSAGE) String exceptionMessage,
                          @Header(KafkaHeaders.ORIGINAL_TIMESTAMP) long timestamp) {
        final var dlqId = Dlq.DlqId.builder()
                .originalTopic(originalTopic)
                .key(msg.key())
                .timestamp(timestamp)
                .exceptionMsg(exceptionMessage)
                .build();
        operations.save(new Dlq(dlqId,msg.value())).subscribe();
    }

}
