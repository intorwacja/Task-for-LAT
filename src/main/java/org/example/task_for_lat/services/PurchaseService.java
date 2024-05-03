package org.example.task_for_lat.services;

import org.example.task_for_lat.repositories.PurchaseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;

    public PurchaseService(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }


}
