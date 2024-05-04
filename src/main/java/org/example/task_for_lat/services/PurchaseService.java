package org.example.task_for_lat.services;

import org.example.task_for_lat.entity.*;
import org.example.task_for_lat.repositories.ProductRepository;
import org.example.task_for_lat.repositories.PromoCodeRepository;
import org.example.task_for_lat.repositories.PurchaseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public void buyItem(Long id, String code){

        Optional<Product> optionalProduct = productRepository.findById(id);
        Optional<PromoCode> optionalPromoCode = promoCodeRepository.findByCode(code);

        if(optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            LocalDateTime currentTime = LocalDateTime.now();
            if(optionalPromoCode.isPresent() && optionalPromoCode.get().getExpDate().isAfter(currentTime)
            && optionalPromoCode.get().getUsageLimit() > 0) {
                PromoCode promoCode = optionalPromoCode.get();
                Purchase purchase = new Purchase();
                purchase.setProduct(product);
                purchase.setPurchaseDate(LocalDateTime.now());
                if(promoCode.getPromoCodeType().equals(PromoCodeType.value)){
                    if(promoCode.getCodeCurrency().equals(product.getCurrency())){
                        purchase.setPromoCode(promoCode);
                        purchase.setPurchasePrice(product.getPrice() - promoCode.getCodeValue());
                        promoCode.setUsageLimit(promoCode.getUsageLimit() - 1);
                        purchaseRepository.save(purchase);
                    }else{
                        purchase.setPurchasePrice(product.getPrice());
                        purchaseRepository.save(purchase);
                    }
                }else{
                    purchase.setPurchasePrice(product.getPrice() - product.getPrice() * (promoCode.getCodeValue() * 0.01));
                    purchase.setPromoCode(promoCode);
                    promoCode.setUsageLimit(promoCode.getUsageLimit() - 1);
                    purchaseRepository.save(purchase);
                }

                purchaseRepository.save(purchase);
            }else {
                Purchase purchase = new Purchase();
                purchase.setPurchaseDate(currentTime);
                purchase.setPurchasePrice(product.getPrice());
                purchase.setProduct(product);
                purchaseRepository.save(purchase);
            }

        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }

    }

    public String generateReport(){
        List<Purchase> purchaseList = purchaseRepository.findAll();

        double totalUSD = 0;
        double totalEUR = 0;
        double totalGBP = 0;

        int totalUSDPurchase = 0;
        int totalEURPurchase = 0;
        int totalGBPPurchase = 0;

        double totalDiscountUSD = 0;
        double totalDiscountEUR = 0;
        double totalDiscountGBP = 0;

        for(Purchase purchase : purchaseList){
            switch (purchase.getProduct().getCurrency()){
                case EUR -> {
                    totalEUR += purchase.getPurchasePrice();
                    totalEURPurchase += 1;
                    if(purchase.getPurchasePrice() != purchase.getProduct().getPrice()){
                        totalDiscountEUR += purchase.getProduct().getPrice() - purchase.getPurchasePrice();
                    }
                }
                case GBP -> {
                    totalGBP += purchase.getPurchasePrice();
                    totalGBPPurchase += 1;
                    if(purchase.getPurchasePrice() != purchase.getProduct().getPrice()){
                        totalDiscountGBP += purchase.getProduct().getPrice() - purchase.getPurchasePrice();
                    }
                }
                case USD -> {
                    totalUSD += purchase.getPurchasePrice();
                    totalUSDPurchase += 1;
                    if(purchase.getPurchasePrice() != purchase.getProduct().getPrice()){
                        totalDiscountUSD += purchase.getProduct().getPrice() - purchase.getPurchasePrice();
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
}
