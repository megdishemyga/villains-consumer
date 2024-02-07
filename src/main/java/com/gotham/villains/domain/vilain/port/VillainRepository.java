package com.gotham.villains.domain.vilain.port;

import com.gotham.villains.domain.vilain.model.Villain;
import reactor.core.publisher.Mono;

public interface VillainRepository {
    Mono<Villain> upsert(Villain villain);
}
