package com.tr.datakeeper.dao;

import com.tr.datakeeper.dto.DataDto;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface DataRepository extends ReactiveMongoRepository<DataDto, String> {

    @Query("{ 'isin': ?0 }")
    Flux<DataDto> findByIsin(String isin);

    @Query(value = "{'isin': ?0}", fields = "{_id : 1}")
    Mono<DataDto> findDistinctFirstByIsin(String isin);

    Mono<Void> deleteById(String id);
}
