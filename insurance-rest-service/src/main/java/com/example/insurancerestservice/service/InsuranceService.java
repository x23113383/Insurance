package com.example.insurancerestservice.service;

import com.example.insurancerestservice.entity.Driver;
import com.example.insurancerestservice.entity.Quote;
import com.example.insurancerestservice.repository.DriverRepository;
import com.example.insurancerestservice.repository.QuoteRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class InsuranceService {
    private final RestTemplate restTemplate;

    private final DriverRepository driverRepository;
    private final QuoteRepository quoteRepository;


    public InsuranceService(RestTemplate restTemplate, DriverRepository driverRepository, QuoteRepository quoteRepository) {
        this.restTemplate = restTemplate;
        this.driverRepository = driverRepository;
        this.quoteRepository = quoteRepository;
    }

    /**
     * This method retrieves a Quote (entity) object from its repository if it exists.
     *
     * @param reference reference if of Quote (entity) object to be retrieved
     * @return Optional Quote object if found
     */
    public Optional<Quote> getQuoteByReference(String reference) {
        return quoteRepository.findById(reference);
    }

    /**
     * This method creates a new quote by breaking down the information provided in the Driver object.
     * Various parameter are analyzed to calculate the insurance factor.
     * <p>
     * This insurance factor is multiplied by a base premium to calculate the final annual insurance.
     * The newly created Quote object is linked to its corresponding Driver object by id.
     * <p>
     * For certain parameters, it is possible that quote cannot be calculated. In such cases, the Quote
     * will have a false success value and users should be redirected to a specialist for custom quotes.
     *
     * @param driver contains driver information parameters to be used for insurance premium calculations
     * @return reference id of newly created quote object
     */
    public String createQuote(Driver driver) {
        Quote quote = new Quote();

        // new quote will be linked to the driver's information
        quote.setDriverId(driver.getId());

        // base premium retrieved from this API
        String apiUrl = "https://storage.googleapis.com/connex-th/insurance_assignment/base_premium.json";

        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
        Gson gson = new Gson();
        JsonObject jsonObj = gson.fromJson(response.getBody(), JsonObject.class);
        double basePremium = jsonObj.get("base_premium").getAsDouble();
        double insuranceFactor = 0.0;

        // factor 1: age
        double ageFactor = 0.0;
        Integer age = driver.getAge();
        if (age < 25) {
            ageFactor = 1.3;
        } else if (age < 40) {
            ageFactor = 1;
        } else if (age < 70) {
            ageFactor = 0.9;
        } else {
            // premium cannot be calculated
            quote.setSuccess(false);
        }

        // factor 2: driving experience
        double drivingExperienceFactor = 0.0;
        Integer drivingExperience = driver.getExperience();
        if (drivingExperience < 2) {
            drivingExperienceFactor = 1.5;
        } else if (drivingExperience < 5) {
            drivingExperienceFactor = 1.3;
        } else if (drivingExperience < 10) {
            drivingExperienceFactor = 1;
        } else {
            drivingExperienceFactor = 0.9;
        }

        // factor 3: faults in the last 5 years
        double driverRecordFactor = 0.0;
        Integer trafficViolations = driver.getFaults();
        if (trafficViolations == 0) {
            driverRecordFactor = 1;
        } else if (trafficViolations == 1) {
            driverRecordFactor = 1.1;
        } else if (trafficViolations >= 2 && trafficViolations <= 3) {
            driverRecordFactor = 1.3;
        } else {
            // premium cannot be calculated
            quote.setSuccess(false);
        }

        // factor 4: insurance claims
        double claimsFactor = 0.0;
        Integer claims = driver.getInsuranceClaims();
        if (claims == 0) {
            claimsFactor = 0.9;
        } else if (claims == 1) {
            claimsFactor = 1.2;
        } else if (claims >= 2 && claims <= 3) {
            claimsFactor = 1.5;
        } else {
            // premium cannot be calculated
            quote.setSuccess(false);
        }

        // factor 5: car value after depreciation
        double carValueFactor = 0.0;
        Double carValue = driver.getVehiclePurchasePrice();
        for (int i = 1; i <= age; i++) {
            if (i <= 3) {
                carValue -= carValue * 0.15;
            } else {
                carValue -= carValue * 0.10;
            }
        }
        if (carValue < 30000.0) {
            carValueFactor = 0.8;
        } else if (carValue < 60000.0) {
            carValueFactor = 1;
        } else if (carValue < 100000.0) {
            carValueFactor = 1.2;
        } else if (carValue < 150000.0) {
            carValueFactor = 1.5;
        } else if (carValue < 200000.0) {
            carValueFactor = 2;
        } else {
            // premium cannot be calculated
            quote.setSuccess(false);
        }

        // factor 6: annual mileage
        double mileageFactor = 0.0;
        Double annualMileage = driver.getVehicleAnnualMileage();
        if (annualMileage < 20000.0) {
            mileageFactor = 0.9;
        } else if (annualMileage < 30000.0) {
            mileageFactor = 1;
        } else if (annualMileage < 50000.0) {
            mileageFactor = 1.1;
        } else {
            mileageFactor = 1.3;
        }

        // factor 7: number of insurances
        double insuranceHistoryFactor = 0.0;
        Integer insuranceHistory = driver.getInsuranceCount();
        if (insuranceHistory == 0) {
            insuranceHistoryFactor = 1.2;
        } else if (insuranceHistory < 2) {
            insuranceHistoryFactor = 1.1;
        } else {
            insuranceHistoryFactor = 1;
        }

        if (quote.getSuccess()) {
            insuranceFactor = ageFactor * drivingExperienceFactor * driverRecordFactor * claimsFactor *
                    carValueFactor * mileageFactor * insuranceHistoryFactor;
        }

        // final insurance factor
        double premium = basePremium * insuranceFactor;
        quote.setPremium(premium);

        driverRepository.save(driver);
        quoteRepository.save(quote);

        return quote.getReference();
    }
}
