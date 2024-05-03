package org.example.task_for_lat.model;
import org.example.task_for_lat.entity.Currency;

public record ProductDto (
    String name,
    double price,
    Currency currency,
    String description
) { }
