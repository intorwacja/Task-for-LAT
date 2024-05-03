package org.example.task_for_lat.services;

import org.example.task_for_lat.entity.PromoCode;
import org.example.task_for_lat.repositories.PromoCodeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@Service
public class PromoCodeService {

    private final PromoCodeRepository promoCodeRepository;

    public PromoCodeService(PromoCodeRepository promoCodeRepository) {
        this.promoCodeRepository = promoCodeRepository;
    }

    public void save(PromoCode promoCode) {
        promoCodeRepository.save(promoCode);
    }

    public List<PromoCode> getPromoCodeList() {
        return promoCodeRepository.findAll();
    }

    public PromoCode findPromoCodeByCode(String code) {
        Optional<PromoCode> promoCodeOptional = promoCodeRepository.findByCode(code);
        return promoCodeOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PromoCode not found"));
    }
}
