package org.example.task_for_lat.services;

import org.example.task_for_lat.entity.Product;
import org.example.task_for_lat.entity.PromoCode;
import org.example.task_for_lat.entity.PromoCodeType;
import org.example.task_for_lat.repositories.ProductRepository;
import org.example.task_for_lat.repositories.PromoCodeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService{

    private final ProductRepository productRepository;
    private final PromoCodeRepository promoCodeRepository;

    public ProductService(ProductRepository productRepository, PromoCodeRepository promoCodeRepository) {
        this.productRepository = productRepository;
        this.promoCodeRepository = promoCodeRepository;
    }

    public void save(Product product) {
            productRepository.save(product);
    }

    public Product findProductByID(Long id){
        Optional<Product> productOptional = productRepository.findById(id);
        return productOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }


    public List<Product> getProductList() {
        return productRepository.findAll();
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product existingProduct = productOptional.get();
            if (updatedProduct.getName() != null) {
                existingProduct.setName(updatedProduct.getName());
            }
            if (updatedProduct.getPrice().compareTo(BigDecimal.ZERO) != 0) {
                existingProduct.setPrice(updatedProduct.getPrice());
            }
            if (updatedProduct.getCurrency() != null) {
                existingProduct.setCurrency(updatedProduct.getCurrency());
            }
            if(updatedProduct.getDescription() != null){
                existingProduct.setDescription(updatedProduct.getDescription());
            }
            return productRepository.save(existingProduct);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
    }

    public ResponseEntity<BigDecimal> calculatePrice(Long id, String code) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        Optional<PromoCode> optionalPromoCode = promoCodeRepository.findByCode(code);

        if (optionalPromoCode.isPresent() && optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            PromoCode promoCode = optionalPromoCode.get();

            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime expirationTime = promoCode.getExpDate();

            if(promoCode.getUsageLimit() > 0 && expirationTime.isAfter(currentTime)) {

                BigDecimal regularPrice = product.getPrice();
                BigDecimal discountPrice;

                if (promoCode.getPromoCodeType().equals(PromoCodeType.value)) {
                    if (promoCode.getCodeCurrency().equals(product.getCurrency())) {
                        discountPrice = regularPrice.subtract(promoCode.getCodeValue());
                        if (discountPrice.compareTo(BigDecimal.ZERO) > 0) {
                            return new ResponseEntity<>(discountPrice, HttpStatus.OK);
                        } else {
                            return new ResponseEntity<>(new BigDecimal("0.00"), HttpStatus.OK);
                        }
                    } else {
                        return new ResponseEntity<>(regularPrice, HttpStatus.BAD_REQUEST);
                    }
                } else {
                    discountPrice = regularPrice.subtract(regularPrice.multiply(promoCode.getCodeValue().multiply(new BigDecimal("0.01"))));
                    return new ResponseEntity<>(discountPrice, HttpStatus.OK);
                }
            }else{
                return new ResponseEntity<>(product.getPrice(), HttpStatus.BAD_REQUEST);
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product or promo code not found");
    }
}
