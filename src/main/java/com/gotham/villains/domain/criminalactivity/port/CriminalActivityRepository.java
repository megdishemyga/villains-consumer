package com.gotham.villains.domain.criminalactivity.port;

import com.gotham.villains.domain.criminalactivity.model.CriminalActivity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CriminalActivityRepository {

    Mono<CriminalActivity> upsert(CriminalActivity criminalActivity);

    Flux<CriminalActivity> find(String villainNickName);
}
