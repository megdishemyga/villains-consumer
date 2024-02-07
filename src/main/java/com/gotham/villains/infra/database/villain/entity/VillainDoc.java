package com.gotham.villains.infra.database.villain.entity;

import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "villains")
@FieldNameConstants
public record VillainDoc(
        @Id String nickName,
        String fullName,
        String status
) {
}
