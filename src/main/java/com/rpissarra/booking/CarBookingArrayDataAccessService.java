package com.rpissarra.booking;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

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
        for (CarBooking cb : bookings) {
            if (cb != null && cb.getId().equals(uuid)) {
                return Optional.of(cb);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteBookingById(UUID id) {
        for (CarBooking booking : bookings) {
            if (booking.getId().equals(id)) {
                bookings.remove(booking);
                return true;
            }
        }
        return false;
    }
}
