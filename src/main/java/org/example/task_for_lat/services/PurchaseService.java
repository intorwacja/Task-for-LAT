package org.example.task_for_lat.services;

import org.example.task_for_lat.entity.*;
import org.example.task_for_lat.repositories.ProductRepository;
import org.example.task_for_lat.repositories.PromoCodeRepository;
import org.example.task_for_lat.repositories.PurchaseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final ProductRepository productRepository;
    private final PromoCodeRepository promoCodeRepository;

    public PurchaseService(PurchaseRepository purchaseRepository, ProductRepository productRepository, PromoCodeRepository promoCodeRepository) {
        this.purchaseRepository = purchaseRepository;
        this.productRepository = productRepository;
        this.promoCodeRepository = promoCodeRepository;
    }

    public ResponseEntity<Void> buyItem(Long id, String code){

        Optional<Product> optionalProduct = productRepository.findById(id);
        Optional<PromoCode> optionalPromoCode = promoCodeRepository.findByCode(code);

        if(optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            LocalDateTime currentTime = LocalDateTime.now();
            Purchase purchase = new Purchase();
            purchase.setProduct(product);
            purchase.setRegularPrice(product.getPrice());
            if(optionalPromoCode.isPresent() && optionalPromoCode.get().getExpDate().isAfter(currentTime)
            && optionalPromoCode.get().getUsageLimit() > 0) {
                PromoCode promoCode = optionalPromoCode.get();
                purchase.setPurchaseDate(LocalDateTime.now());
                if(promoCode.getPromoCodeType().equals(PromoCodeType.value)){
                    if(promoCode.getCodeCurrency().equals(product.getCurrency())){
                        purchase.setPromoCode(promoCode);
                        purchase.setPurchasePrice(checkIfPriceMoreThan0(product.getPrice(),promoCode.getCodeValue()));
                        promoCode.setUsageLimit(promoCode.getUsageLimit() - 1);
                        purchaseRepository.save(purchase);
                        return new ResponseEntity<>(HttpStatus.OK);
                    }else{
                        purchase.setPurchasePrice(product.getPrice());
                        purchaseRepository.save(purchase);
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    }
                }else{
                    purchase.setPurchasePrice(product.getPrice().subtract(product.getPrice().multiply(promoCode.getCodeValue().multiply(new BigDecimal("0.01")))));
                    purchase.setPromoCode(promoCode);
                    promoCode.setUsageLimit(promoCode.getUsageLimit() - 1);
                    purchaseRepository.save(purchase);
                    return new ResponseEntity<>(HttpStatus.OK);
                }
            }else {
                purchase.setPurchaseDate(currentTime);
                purchase.setPurchasePrice(product.getPrice());
                purchaseRepository.save(purchase);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
    }

    public String generateReport(){
        List<Purchase> purchaseList = purchaseRepository.findAll();

        BigDecimal totalUSD = new BigDecimal("0.00");
        BigDecimal totalEUR = new BigDecimal("0.00");
        BigDecimal totalGBP = new BigDecimal("0.00");

        int totalUSDPurchase = 0;
        int totalEURPurchase = 0;
        int totalGBPPurchase = 0;

        BigDecimal totalDiscountUSD = new BigDecimal("0.00");
        BigDecimal totalDiscountEUR = new BigDecimal("0.00");
        BigDecimal totalDiscountGBP = new BigDecimal("0.00");

        for(Purchase purchase : purchaseList){
            switch (purchase.getProduct().getCurrency()){
                case EUR -> {
                    totalEUR = totalEUR.add(purchase.getPurchasePrice());
                    totalEURPurchase += 1;
                    if(!purchase.getPurchasePrice().equals(purchase.getProduct().getPrice())){
                        totalDiscountEUR = totalDiscountEUR.add(purchase.getRegularPrice().subtract(purchase.getPurchasePrice()));
                    }
                }
                case GBP -> {
                    totalGBP = totalGBP.add(purchase.getPurchasePrice());
                    totalGBPPurchase += 1;
                    if(!purchase.getPurchasePrice().equals(purchase.getProduct().getPrice())){
                        totalDiscountGBP = totalDiscountGBP.add(purchase.getRegularPrice().subtract(purchase.getPurchasePrice()));
                    }
                }
                case USD -> {
                    totalUSD = totalUSD.add(purchase.getPurchasePrice());
                    totalUSDPurchase += 1;
                    if(!purchase.getPurchasePrice().equals(purchase.getProduct().getPrice())){
                        totalDiscountUSD = totalDiscountUSD.add(purchase.getRegularPrice().subtract(purchase.getPurchasePrice()));
                    }
                }
            }
        }

        StringBuilder report = new StringBuilder();

        report.append("<pre>");
        report.append("| Currency | Total amount   | Total Discount   | No of purchases |<br>");
        report.append("|----------|----------------|------------------|-----------------|<br>");

        report.append(String.format("| USD      | $%8.2f      | $%8.2f        | %d               |<br>", totalUSD, totalDiscountUSD, totalUSDPurchase));
        report.append(String.format("| EUR      | €%8.2f      | €%8.2f        | %d               |<br>", totalEUR, totalDiscountEUR, totalEURPurchase));
        report.append(String.format("| GBP      | £%8.2f      | £%8.2f        | %d               |<br>", totalGBP, totalDiscountGBP, totalGBPPurchase));
        report.append("</pre>");

        return report.toString();
    }

    private BigDecimal checkIfPriceMoreThan0(BigDecimal price, BigDecimal discount){
        BigDecimal finalPrice = price.subtract(discount);
        if(finalPrice.compareTo(BigDecimal.ZERO) < 0){
            return new BigDecimal("0.00");
        }
        return finalPrice;
    }
}
