package com.gotham.villains.infra.database.dlq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.avro.generic.GenericData;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document("DlqCollection")
public class Dlq {
    @Id
    private DlqId id;
    private GenericData.Record record;

    @Builder
    public record DlqId(long timestamp, String originalTopic, String key, String exceptionMsg) {
    }

}
