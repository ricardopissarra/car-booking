package com.rpissarra.car;

import java.util.Optional;
import java.util.UUID;

public class CarService {

    private CarDao carDao;

    public CarService() {
        this.carDao = new CarDao();
    }

    public Optional<Car> findByCarId(UUID carId) {
        Car[] cars = carDao.findAll();
        for (Car c : cars) {
            if (c.getId().equals(carId)) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }


    public Car[] findAllCars() {
        return carDao.findAll();
    }
}
