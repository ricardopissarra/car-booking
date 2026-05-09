package com.rpissarra.booking;

import java.util.Optional;
import java.util.UUID;

public interface CarBookingDao {
    CarBooking[] findAll();

    void save(CarBooking booking);

    Optional<CarBooking> findById(UUID uuid);

    boolean deleteBookingById(UUID id);
}
