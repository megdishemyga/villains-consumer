package com.gotham.villains.domain.usecase;

import com.gotham.villains.domain.criminalactivity.model.CriminalActivityStatus;
import com.gotham.villains.domain.criminalactivity.port.CriminalActivityRepository;
import com.gotham.villains.domain.vilain.model.Villain;
import com.gotham.villains.domain.vilain.port.VillainRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class VillainDetectedUseCase {

    private final VillainRepository villainRepository;
    private final CriminalActivityRepository criminalActivityRepository;

    public Mono<Villain> process(Villain villain) {
        return villainRepository.upsert(villain)
                .doOnSuccess(this::updateRelatedCriminalActivities);
    }

    private void updateRelatedCriminalActivities(Villain villain) {
        final CriminalActivityStatus criminalActivityStatus = switch (villain.status()) {
            case ACTIVE, ESCAPED -> CriminalActivityStatus.OPEN;
            default -> CriminalActivityStatus.CLOSED;
        };
        criminalActivityRepository.find(villain.nickName())
                .filter(criminalActivity -> criminalActivityStatus != criminalActivity.criminalActivityStatus())
                .flatMap(criminalActivityRepository::upsert)
                .subscribe();
    }

}
