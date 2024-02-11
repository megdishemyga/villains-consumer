package com.gotham.villains.infra.database.crime.mapper;

import com.gotham.villains.domain.crime.model.Crime;
import com.gotham.villains.infra.database.crime.entity.CrimeDoc;

public interface CrimeMapper {
    static Crime toDomain(CrimeDoc crimeDoc) {
        return new Crime(crimeDoc.key().location(), crimeDoc.key().villainNickName(), crimeDoc.criminalActivityStatus());
    }
}
