package com.hanaset.credit.repository;

import com.hanaset.credit.entity.TransactionHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistoryEntity, String> {

    Optional<List<TransactionHistoryEntity>> findByBeforeId(String beforeId);
}
