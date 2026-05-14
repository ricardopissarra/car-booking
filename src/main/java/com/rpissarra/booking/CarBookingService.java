package com.rpissarra.booking;

import com.rpissarra.car.Car;
import com.rpissarra.car.CarService;
import com.rpissarra.user.User;
import com.rpissarra.user.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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

        List<CarBooking> bookings = carBookingDao.findAll();
        for (CarBooking booking : bookings) {
            if (booking != null && booking.getCar().equals(car) && booking.getStatus().equals(BookingStatus.ACTIVE))
                throw new RuntimeException("Car %s is already booked!".formatted(car.getRegNumber()));
        }

        long numberOfDays = ChronoUnit.DAYS.between(startDate, endDate);
        BigDecimal price = car.getRentalPricePerDay().multiply(new BigDecimal(numberOfDays));

        CarBooking booking = new CarBooking(UUID.randomUUID(), user, car, startDate, endDate, price, BookingStatus.ACTIVE, LocalDateTime.now());

        carBookingDao.save(booking);
        return booking;
    }

    public List<CarBooking> getAllUserBookedCars(UUID userId) {
        User user = userService.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No user found with the id %s".formatted(userId)));

        List<CarBooking> bookings = carBookingDao.findAll();
        List<CarBooking> userBookedCars = new ArrayList<>();
        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).getUser().equals(user)) {
                userBookedCars.add(bookings.get(i));
            }
        }
        return userBookedCars;
    }

    public List<Car> getAllAvailableElectricCars() {
        List<Car> cars = carService.findAllCars();
        List<CarBooking> bookings = carBookingDao.findAll();
        List<Car> availableElectricCars = new ArrayList<>();
        for (Car c : cars) {
            if (!c.isElectric()) continue;
            boolean isBooked = false;
            for (CarBooking cb : bookings) {
                if (cb.getCar().equals(c))
                    isBooked = true;
            }
            if (!isBooked) {
                availableElectricCars.add(c);
            }
        }
        return availableElectricCars;
    }

    public List<Car> getAllAvailableCars() {
        List<Car> cars = carService.findAllCars();
        List<CarBooking> bookings = carBookingDao.findAll();
        List<Car> availableElectricCars = new ArrayList<>();
        for (Car c : cars) {
            boolean isBooked = false;
            for (CarBooking cb : bookings) {
                if (cb.getCar().equals(c))
                    isBooked = true;
            }
            if (!isBooked) {
                availableElectricCars.add(c);
            }
        }
        return availableElectricCars;
    }

    public List<CarBooking> getAllBookings() {
        return carBookingDao.findAll();

    }

    public boolean deleteBooking(UUID uuid) {
        carBookingDao.findById(uuid)
                .orElseThrow(() -> new RuntimeException("No booking found with id %s".formatted(uuid)));

        return carBookingDao.deleteBookingById(uuid);
    }
}
