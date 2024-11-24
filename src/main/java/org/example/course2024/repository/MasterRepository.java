package org.example.course2024.repository;

import org.example.course2024.entity.Master;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MasterRepository extends JpaRepository<Master, Long> {
@Query("from Master")
    public List<Master> findAllByPage(PageRequest pageRequest);
}
