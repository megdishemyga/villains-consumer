package com.gotham.villains.infra.database.crime.adapter;

import com.gotham.villains.domain.crime.model.Crime;
import com.gotham.villains.domain.crime.port.CrimeRepository;
import com.gotham.villains.infra.database.crime.entity.CrimeDoc;
import com.gotham.villains.infra.database.crime.entity.CrimeId;
import com.gotham.villains.infra.database.crime.mapper.CrimeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.gotham.villains.infra.database.crime.entity.CrimeDoc.Fields.criminalActivityStatus;
import static com.gotham.villains.infra.database.crime.entity.CrimeDoc.Fields.key;
import static com.gotham.villains.infra.database.crime.entity.CrimeId.Fields.villainNickName;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
@RequiredArgsConstructor
public class CrimeRepositoryAdapter implements CrimeRepository {

    private final ReactiveMongoOperations reactiveMongoOperations;


    @Override
    public Flux<Crime> find(String nickName) {
        final Query query = Query.query(where(key + "." + villainNickName).is(nickName));
        return reactiveMongoOperations.find(query, CrimeDoc.class)
                .map(CrimeMapper::toDomain);
    }

    @Override
    public Mono<Crime> upsert(Crime crimes) {
        final CrimeId crimeId = new CrimeId(crimes.location(), crimes.villainNickName());
        final Query query = Query.query(where(key).is(crimeId));
        final Update update = Update.update(criminalActivityStatus, crimes.criminalActivityStatus())
                .setOnInsert(key, crimeId);
        return reactiveMongoOperations.upsert(query, update, CrimeDoc.class)
                .thenReturn(crimes);
    }
}
