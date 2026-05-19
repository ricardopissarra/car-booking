package com.rpissarra.booking;

import com.rpissarra.car.Car;
import com.rpissarra.car.CarService;
import com.rpissarra.user.User;
import com.rpissarra.user.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

public class CarBookingService {

    private final CarService carService;
    private final UserService userService;
    private final CarBookingDao carBookingDao;

    public CarBookingService(CarService carService, UserService userService, CarBookingDao carBookingDao) {
        this.carService = carService;
        this.userService = userService;
        this.carBookingDao = carBookingDao;
    }


    public CarBooking bookCar(UUID userId, UUID carId, LocalDate startDate, LocalDate
            endDate) {

        User user = userService.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No user found!"));

        Car car = carService.findByCarId(carId)
                .orElseThrow(() -> new RuntimeException("No car found!"));

        if (LocalDate.now().isAfter(startDate) || endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Invalid start or end date!");
        }

        boolean isAlreadyBooked = carBookingDao.findAll().stream()
                .anyMatch(cb -> cb.getCar().equals(car) && cb.getStatus().equals(BookingStatus.ACTIVE));

        if (isAlreadyBooked) throw new RuntimeException("Car %s is already booked!".formatted(car.getRegNumber()));


        long numberOfDays = ChronoUnit.DAYS.between(startDate, endDate);
        BigDecimal price = car.getRentalPricePerDay().multiply(new BigDecimal(numberOfDays));

        CarBooking booking = new CarBooking(UUID.randomUUID(), user, car, startDate, endDate, price, BookingStatus.ACTIVE, LocalDateTime.now());

        carBookingDao.save(booking);
        return booking;
    }

    public List<CarBooking> getAllUserBookedCars(UUID userId) {
        User user = userService.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No user found with the id %s".formatted(userId)));

        return carBookingDao.findAll().stream()
                .filter(cb -> cb.getUser().equals(user))
                .toList();
    }

    public List<Car> getAllAvailableElectricCars() {
        List<CarBooking> bookings = carBookingDao.findAll();
        return carService.findAllCars().stream()
                .filter(c -> c.isElectric())
                .filter(c -> !bookings.stream().map(CarBooking::getCar).toList().contains(c))
                .toList();
    }

    public List<Car> getAllAvailableCars() {
        List<CarBooking> bookings = carBookingDao.findAll();
        return carService.findAllCars().stream()
                .filter(c -> !bookings.stream().map(CarBooking::getCar).toList().contains(c))
                .toList();
    }

    public List<CarBooking> getAllBookings() {
        return carBookingDao.findAll();

    }

    public boolean deleteBooking(UUID uuid) {
        CarBooking booking = carBookingDao.findById(uuid)
                .orElseThrow(() -> new RuntimeException("No booking found with id %s".formatted(uuid)));

        return carBookingDao.deleteBooking(booking);
    }
}
