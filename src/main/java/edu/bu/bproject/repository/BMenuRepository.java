package edu.bu.bproject.repository;

import edu.bu.bproject.entity.BMenus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BMenuRepository extends JpaRepository<BMenus, Integer> {
    public List<BMenus> findBMenuBySellerId(Integer id);
}
