package com.gotham.villains.infra.kafka.inbound;

import com.gotham.VillainEvent;
import com.gotham.VillainStatus;
import com.gotham.villains.infra.database.villain.entity.VillainDoc;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;


@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class VillainsConsumerTest {

    @Container
    @ServiceConnection
    static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:latest")
    );

    @Container
    @ServiceConnection
    static final MongoDBContainer mongodb = new MongoDBContainer(
            DockerImageName.parse("mongo:latest")
    ).withExposedPorts(27017);

    @Autowired
    private KafkaTemplate<String, VillainEvent> kafkaTemplate;
    @Autowired
    private ReactiveMongoOperations mongoOperations;

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @BeforeEach
    void setUp() {
        kafka.start();
        mongodb.start();
    }

    @AfterEach
    void tearDown() {
        kafka.stop();
        mongodb.stop();
    }

    @Test
    void test() {
        final VillainEvent villainEvent = VillainEvent.newBuilder()
                .setNickname("Joker")
                .setFullName("Jack Napier")
                .setAge(54)
                .setStatus(VillainStatus.ACTIVE)
                .build();
        kafkaTemplate.send("villains", "GDCPD", villainEvent);
        await()
                .pollInterval(Duration.ofSeconds(2))
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    final Query query = Query.query(where("nickName").is("Joker"));
                    StepVerifier.create(mongoOperations.findOne(query, VillainDoc.class))
                            .assertNext(villainDoc -> assertThat(villainDoc)
                                    .extracting(VillainDoc::fullName, VillainDoc::status)
                                    .containsExactly("Jack Napier", VillainStatus.ACTIVE.name()))
                            .verifyComplete();
                });
    }
}



