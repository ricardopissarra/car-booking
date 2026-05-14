package com.rpissarra.car;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CarFileDataAccessService implements CarDao {

   private final String filePath;

    public CarFileDataAccessService(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Car> findAll() {
        List<Car> carList = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
                while (true) {
                    carList.add((Car) ois.readObject());
                }
        } catch (EOFException ignored){
            // end of file was reached, all lines added to list
        } catch (FileNotFoundException e){
            System.err.println("File doesn't exist yet.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error reading users from file.");
        }
        return carList;
    }

}
