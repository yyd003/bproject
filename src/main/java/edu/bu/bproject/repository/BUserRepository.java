package edu.bu.bproject.repository;

import edu.bu.bproject.entity.BUsers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BUserRepository extends JpaRepository<BUsers, Integer> {
    List<BUsers> findBUserByName(String name);
}
