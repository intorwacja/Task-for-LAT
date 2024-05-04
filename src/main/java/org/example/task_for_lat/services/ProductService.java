package org.example.task_for_lat.services;

import org.example.task_for_lat.entity.Product;
import org.example.task_for_lat.entity.PromoCode;
import org.example.task_for_lat.entity.PromoCodeType;
import org.example.task_for_lat.model.ProductDto;
import org.example.task_for_lat.repositories.ProductRepository;
import org.example.task_for_lat.repositories.PromoCodeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
            if (updatedProduct.getPrice() != 0) {
                existingProduct.setPrice(updatedProduct.getPrice());
            }
            if (updatedProduct.getCurrency() != null) {
                existingProduct.setCurrency(updatedProduct.getCurrency());
            }
            return productRepository.save(existingProduct);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
    }

    public ProductDto productToProductDto(Product product) {
        ProductDto productDto = new ProductDto(product.getName(), product.getPrice(), product.getCurrency(), product.getDescription());
        return productDto;
    }

    public double calculatePrice(Long id, String code) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        Optional<PromoCode> optionalPromoCode = promoCodeRepository.findByCode(code);

        if (optionalPromoCode.isPresent() && optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            PromoCode promoCode = optionalPromoCode.get();

            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime expirationTime = promoCode.getExpDate();

            if(promoCode.getUsageLimit() > 0 && expirationTime.isAfter(currentTime)) {

                double regularPrice = product.getPrice();
                double discountPrice = 0;

                if (promoCode.getPromoCodeType().equals(PromoCodeType.value)) {
                    if (promoCode.getCodeCurrency().equals(product.getCurrency())) {
                        discountPrice = regularPrice - promoCode.getCodeValue();
                        if (discountPrice > 0) {
                            return discountPrice;
                        } else {
                            return 0;
                        }
                    } else {
                        return regularPrice;
                    }
                } else {
                    discountPrice = regularPrice - (regularPrice * (promoCode.getCodeValue() * 0.01));
                    return discountPrice;
                }
            }else{
                return product.getPrice();
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product or promo code not found");
    }
}
