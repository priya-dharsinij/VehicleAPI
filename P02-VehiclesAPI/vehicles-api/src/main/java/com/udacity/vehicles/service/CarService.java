package com.udacity.vehicles.service;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implements the car service create, read, update or delete
 * information about vehicles, as well as gather related
 * location and price data when desired.
 */
@Service
public class CarService {

    private final CarRepository repository;
    private final PriceClient priceClient;
    private final MapsClient mapClient;

    public CarService(CarRepository repository, PriceClient priceClient, MapsClient mapClient) {
        /**
         * TODO: Add the Maps and Pricing Web Clients you create
         *   in `VehiclesApiApplication` as arguments and set them here.
         */
        this.repository = repository;
        this.priceClient = priceClient;
        this.mapClient = mapClient;
    }

    /**
     * Gathers a list of all vehicles
     * @return a list of all vehicles in the CarRepository
     */
    public List<Car> list() {

        List<Car> cars = repository.findAll();

        cars.stream().forEach((car -> {
            setPriceAndLocation(car);
        }));

        return cars;
    }

    /**
     * Gets car information by ID (or throws exception if non-existent)
     * @param id the ID number of the car to gather information on
     * @return the requested car's information, including location and price
     */
    public Car findById(Long id) {
        /**
         * TODO: Find the car by ID from the `repository` if it exists.
         *   If it does not exist, throw a CarNotFoundException
         *   Remove the below code as part of your implementation.
         */
        Optional<Car> optionalCar = repository.findById(id);
        Car car = optionalCar.orElseThrow(CarNotFoundException::new );

        return setPriceAndLocation(car);
    }

    /**
     * Either creates or updates a vehicle, based on prior existence of car
     * @param car A car object, which can be either new or existing
     * @return the new/updated car is stored in the repository
     */
    public Car save(Car car) {
        if (car.getId() != null) {

            return repository.findById(car.getId())
                    .map(carToBeUpdated -> {
                        carToBeUpdated.setCondition(car.getCondition());
                        carToBeUpdated.setDetails(car.getDetails());
                        carToBeUpdated.setLocation(car.getLocation());
                        return setPriceAndLocation(repository.save(carToBeUpdated));
                    }).orElseThrow(CarNotFoundException::new);
        }

        Car newCar = repository.save(car);
        return setPriceAndLocation(newCar);
    }

    /**
     * Deletes a given car by ID
     * @param id the ID number of the car to delete
     */
    public void delete(Long id) {
        /**
         * TODO: Find the car by ID from the `repository` if it exists.
         *   If it does not exist, throw a CarNotFoundException
         */

        Optional<Car> optionalCar = repository.findById(id);
        Car car = optionalCar.orElseThrow(CarNotFoundException::new );

        repository.deleteById(id);
    }

    private Car setPriceAndLocation(Car car){
        Location location = mapClient.getAddress(car.getLocation());
        car.setLocation(location);

        String price = priceClient.getPrice(car.getId());
        car.setPrice(price);

        return car;
    }
}
