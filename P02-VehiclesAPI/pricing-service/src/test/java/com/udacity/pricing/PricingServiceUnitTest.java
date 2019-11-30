package com.udacity.pricing;

import com.udacity.pricing.domain.price.Price;
import com.udacity.pricing.domain.price.PriceRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.util.Optional;


import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class PricingServiceUnitTest {

    @Autowired
    private MockMvc mvc;

    private Price price;

    private PriceRepository priceRepositoryMock;



    /**
     * Creates pre-requisites for testing, such as an example car.
     */
    @Before
    public void setup() {
        priceRepositoryMock = Mockito.mock(PriceRepository.class);
        price = new Price();
        price.setVehicleId(1L);
        price.setCurrency("USD");
        price.setPrice(new BigDecimal(10000.94).setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void getPriceByVehicleId() throws Exception {

        when(priceRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        mvc.perform(get(new URI("/services/price")).param("vehicleId","1")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price", is(price.getPrice().doubleValue())));

    }

}
