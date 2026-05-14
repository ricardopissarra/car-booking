package com.rpissarra.car;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CarService {

    private CarDao carDao;

    public CarService(CarDao carDao) {
        this.carDao = carDao;
    }


    public Optional<Car> findByCarId(UUID carId) {
        List<Car> cars = carDao.findAll();
        for (Car c : cars) {
            if (c.getId().equals(carId)) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }


    public List<Car> findAllCars() {
        return carDao.findAll();
    }
}
