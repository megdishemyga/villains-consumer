package com.gotham.villains.infra.database.criminalactivity.mapper;

import com.gotham.villains.domain.criminalactivity.model.CriminalActivity;
import com.gotham.villains.infra.database.criminalactivity.entity.CriminalActivityDoc;

public interface CriminalActivityMapper {
    static CriminalActivity toDomain(CriminalActivityDoc criminalActivityDoc) {
        return new CriminalActivity(criminalActivityDoc.key().location(), criminalActivityDoc.key().villainNickName(), criminalActivityDoc.criminalActivityStatus());
    }
}
