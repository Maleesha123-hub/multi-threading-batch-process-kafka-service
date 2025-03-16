package com.pdev.multi_threading_batch_process_kafka_service.repository;

import com.pdev.multi_threading_batch_process_kafka_service.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query("SELECT p.phone FROM Person p")
    List<String> findPhone();
}
