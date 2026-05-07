package com.rpissarra.booking;

import com.rpissarra.car.Car;
import com.rpissarra.car.CarService;
import com.rpissarra.user.User;
import com.rpissarra.user.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class CarBookingService {

    private CarService carService;
    private UserService userService;
    private CarBookingDao carBookingDao;

    public CarBookingService() {
        this.carService = new CarService();
        this.userService = new UserService();
        this.carBookingDao = new CarBookingDao();
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

        CarBooking[] bookings = carBookingDao.findAll();
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

    public CarBooking[] getAllUserBookedCars(UUID userId) {
        User user = userService.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No user found with the id %s".formatted(userId)));

        CarBooking[] bookings = carBookingDao.findAll();

        int numOfUserBookings = 0;
        for (int i = 0; i < bookings.length; i++) {
            if (bookings[i] != null && bookings[i].getUser().equals(user)) {
                ++numOfUserBookings;
            }
        }
        if (numOfUserBookings == 0) {
            return new CarBooking[0];
        }

        CarBooking[] userBookings = new CarBooking[numOfUserBookings];
        int index = 0;
        for (CarBooking cb : bookings) {
            if (cb != null && cb.getUser().equals(user)) {
                userBookings[index++] = cb;
            }
        }
        return userBookings;
    }

    public Car[] getAllAvailableElectricCars() {
        Car[] cars = carService.findAllCars();
        CarBooking[] bookings = carBookingDao.findAll();
        int numAvailableCars = 0;
        for (Car c : cars) {
            if (!c.isElectric()) continue;
            boolean isBooked = false;
            for (CarBooking cb : bookings) {
                if (cb != null && cb.getCar().equals(c))
                    isBooked = true;
            }
            if (!isBooked) {
                numAvailableCars++;
            }
        }

        if (numAvailableCars == 0) {
            return new Car[0];
        }

        Car[] availableCars = new Car[numAvailableCars];
        int index = 0;
        for (Car c : cars) {
            if (!c.isElectric()) continue;
            boolean isBooked = false;
            for (CarBooking cb : bookings) {
                if (cb != null && cb.getCar().equals(c))
                    isBooked = true;
            }
            if (!isBooked) {
                availableCars[index++] = c;
            }
        }

        return availableCars;
    }

    public Car[] getAllAvailableCars() {
        Car[] cars = carService.findAllCars();
        CarBooking[] bookings = carBookingDao.findAll();
        int numAvailableCars = 0;
        for (Car c : cars) {
            boolean isBooked = false;
            for (CarBooking cb : bookings) {
                if (cb != null && cb.getCar().equals(c))
                    isBooked = true;
            }
            if (!isBooked) {
                numAvailableCars++;
            }
        }

        if (numAvailableCars == 0) {
            return new Car[0];
        }

        Car[] availableCars = new Car[numAvailableCars];
        int index = 0;
        for (Car c : cars) {
            boolean isBooked = false;
            for (CarBooking cb : bookings) {
                if (cb != null && cb.getCar().equals(c))
                    isBooked = true;
            }
            if (!isBooked) {
                availableCars[index++] = c;
            }
        }

        return availableCars;
    }

    public CarBooking[] getAllBookings() {
        CarBooking[] bookings = carBookingDao.findAll();
        int currActiveBookings = 0;
        for (CarBooking cb : bookings) {
            if (cb != null)
                ++currActiveBookings;
        }
        if (currActiveBookings == 0) {
            return new CarBooking[0];
        }

        CarBooking[] actualBookings = new CarBooking[currActiveBookings];
        int index = 0;
        for (int i = 0; i < bookings.length; i++) {
            if (bookings[i] != null) {
                actualBookings[index++] = bookings[i];
            }
        }
        return actualBookings;

    }

    public boolean deleteBooking(UUID uuid) {
        carBookingDao.findById(uuid)
                .orElseThrow(() -> new RuntimeException("No booking found with id %s".formatted(uuid)));

        return carBookingDao.deleteBookingById(uuid);
    }
}
