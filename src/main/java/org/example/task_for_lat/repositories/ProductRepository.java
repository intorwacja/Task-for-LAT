package org.example.task_for_lat.repositories;

import org.example.task_for_lat.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>{

}
