package com.rpissarra.booking;

import java.util.*;

public class CarBookingArrayDataAccessService implements CarBookingDao {

    private static List<CarBooking> bookings;

    static {
        bookings = new ArrayList<>();
    }

    @Override
    public List<CarBooking> findAll() {
        return bookings;
    }

    @Override
    public void save(CarBooking booking) {
        bookings.add(booking);
    }

    @Override
    public Optional<CarBooking> findById(UUID uuid) {
        return bookings.stream()
                .filter(cb -> cb.getId().equals(uuid))
                .findFirst();
    }

    @Override
    public boolean deleteBooking(CarBooking booking) {
        return bookings.remove(booking);
    }
}
