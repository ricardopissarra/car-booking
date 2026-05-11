package com.rpissarra.car;

import java.io.*;

public class CarFileDataAccessService implements CarDao {

   private final String filePath;

    public CarFileDataAccessService(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Car[] findAll() {
        try (FileInputStream fis = new FileInputStream(filePath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            int length = ois.readInt();
            Car[] cars = new Car[length];
            int index = 0;
            for (Car c : cars) {
                cars[index++] =(Car) ois.readObject();
            }
            return cars;
        } catch (FileNotFoundException e){
            System.err.println("File doesn't exist yet.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error reading users from file.");
        }
        return new Car[0];
    }

}
