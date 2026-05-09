package com.rpissarra.booking;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class CarBookingArrayDataAccessService implements CarBookingDao {

    private static CarBooking[] bookings;
    private static int capacity = 2;

    static {
        bookings = new CarBooking[capacity];
    }

    @Override
    public CarBooking[] findAll() {
        return bookings;
    }

    @Override
    public void save(CarBooking booking) {
        int nextAvailableIndex = findNextAvailableIndex();
        if (nextAvailableIndex == -1) {
            // Array is full, so I double de capacity and copy the current arr
            // to the new arr
            int newCapacity = capacity*2;
            bookings = Arrays.copyOf(bookings, newCapacity);
            nextAvailableIndex = capacity;
            capacity = newCapacity;
        }
        bookings[nextAvailableIndex] = booking;
    }

    private int findNextAvailableIndex() {
        for (int i = 0; i < bookings.length; i++) {
            if (bookings[i] == null) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Optional<CarBooking> findById(UUID uuid) {
        for (CarBooking cb : bookings) {
            if (cb != null && cb.getId().equals(uuid)) {
                return Optional.of(cb);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteBookingById(UUID id) {

        for (int i = 0; i < bookings.length; i++) {
            if (bookings[i] != null && bookings[i].getId().equals(id)) {
                bookings[i] = null;
                return true;
            }
        }
        return false;
    }
}
