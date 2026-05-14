package com.rpissarra.booking;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CarBookingFileDataAccessService implements CarBookingDao {

    private final String filePath;

    public CarBookingFileDataAccessService(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<CarBooking> findAll() {
        List<CarBooking> bookings = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            while (true) {
                bookings.add((CarBooking) ois.readObject());
            }
        } catch (EOFException ignore) {
            // end of file was reached, all lines added to list
        } catch (FileNotFoundException e){
            System.err.println("File doesn't exist yet.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error reading bookings from file.");
        }
        return bookings;
    }

    @Override
    public void save(CarBooking booking) {
        List<CarBooking> bookings = findAll();
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            for (CarBooking carBooking : bookings) {
                oos.writeObject(carBooking);
            }
            oos.writeObject(booking);
        } catch (IOException e) {
            throw new RuntimeException("Error saving booking");
        }
    }

    @Override
    public Optional<CarBooking> findById(UUID uuid) {
        for (CarBooking cb : findAll()) {
            if (cb != null && cb.getId().equals(uuid)) {
                return Optional.of(cb);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteBookingById(UUID id) {
        List<CarBooking> bookings = findAll();

        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            for (CarBooking cb : bookings) {
                if (cb != null && cb.getId().equals(id)) {
                    continue;
                }
                oos.writeObject(cb);
            }

            return true;
        } catch (IOException e) {
            System.err.println("Error deleting booking with id %s".formatted(id));
            return false;
        }
    }
}
