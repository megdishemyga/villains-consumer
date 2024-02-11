package com.gotham.villains.domain.crime.port;

import com.gotham.villains.domain.crime.model.Crime;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CrimeRepository {

    Mono<Crime> upsert(Crime crimes);

    Flux<Crime> find(String villainNickName);
}
