package com.rpissarra;

import com.rpissarra.booking.CarBooking;
import com.rpissarra.booking.CarBookingDao;
import com.rpissarra.booking.CarBookingFileDataAccessService;
import com.rpissarra.booking.CarBookingService;

import com.rpissarra.car.CarDao;
import com.rpissarra.car.CarFileDataAccessService;
import com.rpissarra.car.CarService;
import com.rpissarra.user.UserDao;
import com.rpissarra.user.UserFileDataAccessService;
import com.rpissarra.user.UserService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.UUID;

public class Main {

    public static final String BOOKINGS_FILE_PATH = "src/main/java/com/rpissarra/bookings.dat";
    private static final String USERS_FILE_PATH = "src/main/java/com/rpissarra/users.dat";
    private static final String CARS_FILE_PATH = "src/main/java/com/rpissarra/cars.dat";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        CarDao carDao = new CarFileDataAccessService(CARS_FILE_PATH);
        CarService carService = new CarService(carDao);

        UserDao userDao = new UserFileDataAccessService(USERS_FILE_PATH);
        UserService userService = new UserService(userDao);

        CarBookingDao carBookingDao = new CarBookingFileDataAccessService(BOOKINGS_FILE_PATH);
        CarBookingService carBookingService = new CarBookingService(carService, userService, carBookingDao);

        boolean loopIsActive = true;
        while (loopIsActive) {
            System.out.println("""
                    1 - Book Car
                    2 - View All User Booked Cars
                    3 - View All Bookings
                    4 - View Available Cars
                    5 - View Available Electric Cars
                    6 - View all users
                    7 - Delete booking
                    8 - Exit
                    """);
            try {
                int input = scanner.nextInt();
                switch (input) {
                    case 1 -> bookCar(carBookingService, userService, carService, scanner);
                    case 2 -> viewAllUserBookedCars(scanner, userService, carBookingService);
                    case 3 -> System.out.println(Arrays.toString(carBookingService.getAllBookings()));
                    case 4 -> System.out.println(Arrays.toString(carBookingService.getAllAvailableCars()));
                    case 5 -> System.out.println(Arrays.toString(carBookingService.getAllAvailableElectricCars()));
                    case 6 -> System.out.println(Arrays.toString(userService.findAllUsers()));
                    case 7 -> deleteBooking(carBookingService, scanner);
                    case 8 -> loopIsActive = false;
                    default -> System.out.println("Invalid option");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input!");
                scanner.nextLine();
            }
        }


    }

    private static void deleteBooking(CarBookingService carBookingService,
                                      Scanner scanner) {
        try {
            scanner.nextLine();
            CarBooking[] bookings = carBookingService.getAllBookings();
            if (bookings.length == 0) {
                System.out.println("There are not active bookings");
            } else {
                System.out.println(Arrays.toString(bookings));
                System.out.print("Select a booking to delete and enter the booking id: ");
                String bookingId = scanner.nextLine();

                if(carBookingService.deleteBooking(UUID.fromString(bookingId))) {
                    System.out.println("Booking %s deleted successfully".formatted(bookingId));
                } else {
                    System.out.println("Unable to delete booking with id: %s".formatted(bookingId));
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void bookCar(CarBookingService carBookingService,
                                UserService userService,
                                CarService carService,
                                Scanner scanner) {
        try {
            scanner.nextLine();
            System.out.println(Arrays.toString(userService.findAllUsers()));
            System.out.print("Select a user and enter the user id: ");
            String userId = scanner.nextLine();

            System.out.println(Arrays.toString(carService.findAllCars()));
            System.out.print("Select a car and enter the car id: ");
            String carId = scanner.nextLine();

            System.out.print("Enter the start date for the booking: ");
            String startDateStr = scanner.nextLine();

            System.out.print("Enter the end date for the booking: ");
            String endDateStr = scanner.nextLine();

            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate = LocalDate.parse(endDateStr);

            System.out.println(carBookingService.bookCar(
                    UUID.fromString(userId),
                    UUID.fromString(carId),
                    startDate,
                    endDate
            ));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    private static void viewAllUserBookedCars(Scanner scanner,
                                              UserService userService,
                                              CarBookingService carBookingService) {
        try {
            scanner.nextLine();
            System.out.println(Arrays.toString(userService.findAllUsers()));
            System.out.print("Select a user and enter the user id: ");

            String userId = scanner.nextLine();
            System.out.println(Arrays.toString(carBookingService.getAllUserBookedCars(UUID.fromString(userId))));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
