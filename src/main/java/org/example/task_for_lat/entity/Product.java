package org.example.task_for_lat.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private double price;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Currency currency;

    private String description;

    @ManyToMany
    Set<PromoCode> avaliblePromoCodes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Set<PromoCode> getAvaliblePromoCodes() {
        return avaliblePromoCodes;
    }

    public void setAvaliblePromoCodes(Set<PromoCode> avaliblePromoCodes) {
        this.avaliblePromoCodes = avaliblePromoCodes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
