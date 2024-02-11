package com.gotham.villains.infra.kafka.mapper;

import com.gotham.VillainEvent;
import com.gotham.villains.domain.vilain.model.Villain;
import com.gotham.villains.domain.vilain.model.VillainStatus;

public interface VillainEventMapper {
    static Villain toDomain(VillainEvent villainEvent) {
        final String nickName = String.valueOf(villainEvent.getNickname());
        final String fullName = String.valueOf(villainEvent.getFullName());
        final String status = villainEvent.getStatus().name();
        final Integer age = villainEvent.getAge();
        return new Villain(nickName, fullName, age, VillainStatus.valueOf(status));
    }
}
