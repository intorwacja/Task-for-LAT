package org.example.task_for_lat.controllers;

import org.example.task_for_lat.services.PurchaseService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {
    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping("/{id}")
    public void buyItem(@PathVariable Long id, @RequestParam String code) {
        purchaseService.buyItem(id, code);
    }

    @GetMapping("/report")
    public String report() {
        return purchaseService.generateReport();
    }

}
