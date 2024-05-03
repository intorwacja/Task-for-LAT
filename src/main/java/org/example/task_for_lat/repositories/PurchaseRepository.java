package org.example.task_for_lat.repositories;

import org.example.task_for_lat.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

}
