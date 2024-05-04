package org.example.task_for_lat.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Entity
public class PromoCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]{3,24}$", message = "Code must contain only alphanumeric characters with no spaces and must be 3-24 characters long.")
    private String code;

    @Enumerated(EnumType.STRING)
    @NotNull
    private PromoCodeType promoCodeType;

    @NotNull
    private int usageLimit;

    @Enumerated(EnumType.STRING)
    private Currency codeCurrency;

    @NotNull
    private double codeValue;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private LocalDateTime expDate;

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public PromoCodeType getPromoCodeType() {
        return promoCodeType;
    }

    public void setPromoCodeType(PromoCodeType type) {
        this.promoCodeType = type;
    }

    public int getUsageLimit() {
        return usageLimit;
    }

    public void setUsageLimit(int usageLimit) {
        this.usageLimit = usageLimit;
    }

    public double getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(double value) {
        this.codeValue = value;
    }

    public LocalDateTime getExpDate() {
        return expDate;
    }

    public void setExpDate(LocalDateTime expDate) {
        this.expDate = expDate;
    }

    public Currency getCodeCurrency() {
        return codeCurrency;
    }

    public void setCodeCurrency(Currency codeCurrency) {
        this.codeCurrency = codeCurrency;
    }
}
