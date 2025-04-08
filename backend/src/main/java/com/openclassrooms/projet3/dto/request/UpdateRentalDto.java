package com.openclassrooms.projet3.dto.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
public class UpdateRentalDto  {
    private String name;
    private BigDecimal surface;
    private BigDecimal price;
    private String description;
}
