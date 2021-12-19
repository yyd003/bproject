package edu.bu.bproject.repository;

import edu.bu.bproject.entity.BRecords;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BRecordRepository extends JpaRepository<BRecords, Integer> {
    public List<BRecords> findBRecordBySellerId(Integer id);

    public List<BRecords> findBRecordByCustomerId(Integer id);
}
