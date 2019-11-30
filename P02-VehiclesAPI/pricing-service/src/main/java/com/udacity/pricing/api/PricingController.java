package com.udacity.pricing.api;

import com.udacity.pricing.domain.price.Price;
import com.udacity.pricing.domain.price.PriceRepository;
import com.udacity.pricing.service.PriceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Implements a REST-based controller for the pricing service.
 */
@RestController
public class PricingController {

    private PriceRepository priceRepository;

    @Autowired
    public void setPriceRepository(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @GetMapping("/services/price")
    public ResponseEntity<Price> getPriceById(@RequestParam Long vehicleId) throws PriceException {
        Optional<Price> optionalPrice = priceRepository.findById(vehicleId);
        Price price = optionalPrice.orElseThrow(() -> new PriceException("Cannot find price for Vehicle"+vehicleId));
        return ResponseEntity.ok(price);
    }

}
