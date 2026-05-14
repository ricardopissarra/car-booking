package com.rpissarra.user;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserFileDataAccessService implements UserDao {

    private final String filePath;

    public UserFileDataAccessService(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<User> findAll() {
        List<User> userList = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            while (true) {
                userList.add((User) ois.readObject());
            }
        } catch (EOFException ignored){
            // end of file was reached, all lines added to list
        } catch (FileNotFoundException e){
            System.err.println("File doesn't exist yet.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error reading users from file.");
        }
        return userList;
    }
}
