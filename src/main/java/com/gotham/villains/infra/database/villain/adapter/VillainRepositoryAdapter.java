package com.gotham.villains.infra.database.villain.adapter;

import com.gotham.villains.domain.vilain.model.Villain;
import com.gotham.villains.domain.vilain.port.VillainRepository;
import com.gotham.villains.infra.database.villain.entity.VillainDoc;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static com.gotham.villains.infra.database.villain.entity.VillainDoc.Fields.fullName;
import static com.gotham.villains.infra.database.villain.entity.VillainDoc.Fields.nickName;
import static com.gotham.villains.infra.database.villain.entity.VillainDoc.Fields.status;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
@RequiredArgsConstructor
public class VillainRepositoryAdapter implements VillainRepository {
    private final ReactiveMongoOperations operations;

    @Override
    public Mono<Villain> upsert(Villain villain) {
        final Query query = Query.query(where(nickName).is(villain.nickName()));
        final Update update = Update.update(fullName, villain.fullName())
                .set(status, villain.status())
                .setOnInsert(nickName, villain.nickName());
        return operations.upsert(query, update, VillainDoc.class)
                .thenReturn(villain);
    }
}
