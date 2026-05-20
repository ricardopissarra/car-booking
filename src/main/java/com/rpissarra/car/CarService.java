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
        return findAllCars().stream()
                .filter(c -> c.getId().equals(carId))
                .findFirst();
    }


    public List<Car> findAllCars() {
        return carDao.findAll();
    }
}
