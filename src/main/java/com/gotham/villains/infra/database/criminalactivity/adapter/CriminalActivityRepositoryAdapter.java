package com.gotham.villains.infra.database.criminalactivity.adapter;

import com.gotham.villains.domain.criminalactivity.model.CriminalActivity;
import com.gotham.villains.domain.criminalactivity.port.CriminalActivityRepository;
import com.gotham.villains.infra.database.criminalactivity.entity.CriminalActivityDoc;
import com.gotham.villains.infra.database.criminalactivity.entity.CriminalActivityKey;
import com.gotham.villains.infra.database.criminalactivity.mapper.CriminalActivityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.gotham.villains.infra.database.criminalactivity.entity.CriminalActivityDoc.Fields.criminalActivityStatus;
import static com.gotham.villains.infra.database.criminalactivity.entity.CriminalActivityDoc.Fields.key;
import static com.gotham.villains.infra.database.criminalactivity.entity.CriminalActivityKey.Fields.villainNickName;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
@RequiredArgsConstructor
public class CriminalActivityRepositoryAdapter implements CriminalActivityRepository {

    private final ReactiveMongoOperations reactiveMongoOperations;


    @Override
    public Flux<CriminalActivity> find(String nickName) {
        final Query query = Query.query(where(key + "." + villainNickName).is(nickName));
        return reactiveMongoOperations.find(query, CriminalActivityDoc.class)
                .map(CriminalActivityMapper::toDomain);
    }

    @Override
    public Mono<CriminalActivity> upsert(CriminalActivity criminalActivity) {
        final CriminalActivityKey criminalActivityKey = new CriminalActivityKey(criminalActivity.location(), criminalActivity.villainNickName());
        final Query query = Query.query(where(key).is(criminalActivityKey));
        final Update update = Update.update(criminalActivityStatus, criminalActivity.criminalActivityStatus())
                .setOnInsert(key, criminalActivityKey);
        return reactiveMongoOperations.upsert(query, update, CriminalActivityDoc.class)
                .thenReturn(criminalActivity);
    }
}
