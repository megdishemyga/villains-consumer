package com.gotham.villains.infra.database.criminalactivity.entity;

import com.gotham.villains.domain.criminalactivity.model.CriminalActivityStatus;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "criminalActivities")
@FieldNameConstants
public record CriminalActivityDoc(
        @Id CriminalActivityKey key,
        CriminalActivityStatus criminalActivityStatus
) {
}
