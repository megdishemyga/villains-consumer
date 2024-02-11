package com.gotham.villains.infra.database.crime.entity;

import com.gotham.villains.domain.crime.model.CrimeStatus;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "criminalActivities")
@FieldNameConstants
public record CrimeDoc(
        @Id CrimeId key,
        CrimeStatus criminalActivityStatus
) {
}
