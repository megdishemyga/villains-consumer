package com.gotham.villains.domain.usecase;

import com.gotham.villains.domain.crime.port.CrimeRepository;
import com.gotham.villains.domain.vilain.model.Villain;
import com.gotham.villains.domain.vilain.port.VillainRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class VillainDetectedUseCase {

    private final VillainRepository villainRepository;
    private final CrimeRepository crimeRepository;

    public Mono<Villain> process(Villain villain) {
        return villainRepository.upsert(villain);
    }

}
