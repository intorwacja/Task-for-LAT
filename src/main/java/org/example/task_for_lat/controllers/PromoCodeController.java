package org.example.task_for_lat.controllers;

import org.example.task_for_lat.entity.PromoCode;
import org.example.task_for_lat.services.PromoCodeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promocodes")
public class PromoCodeController {
    private final PromoCodeService promoCodeService;

    PromoCodeController(PromoCodeService promoCodeService) {
        this.promoCodeService = promoCodeService;
    }

    @GetMapping("/{code}")
    public PromoCode findPromoCodeByCode(@PathVariable String code) {
        return promoCodeService.findPromoCodeByCode(code);
    }

    @PostMapping("/add")
    public void addPromoCode(@RequestBody PromoCode promoCode) {
        promoCodeService.save(promoCode);
    }

    @GetMapping("/all")
    public List<PromoCode> findAll() {
        return promoCodeService.getPromoCodeList();
    }


}
