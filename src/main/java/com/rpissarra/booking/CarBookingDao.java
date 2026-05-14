package com.rpissarra.booking;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarBookingDao {
    List<CarBooking> findAll();

    void save(CarBooking booking);

    Optional<CarBooking> findById(UUID uuid);

    boolean deleteBookingById(UUID id);
}
