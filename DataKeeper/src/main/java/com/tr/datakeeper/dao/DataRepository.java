package com.tr.datakeeper.dao;

import com.tr.datakeeper.entity.Data;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface DataRepository extends ReactiveMongoRepository<Data, String> {

    @Query("select * from Data d where d.isin=:isin")
    Flux<Data> findByIsin(@Param("isin") String isin);

    @Query("delete from Data d where d.isin=:isin")
    Mono<Void> deleteByIsin(@Param("isin") String isin);
}
