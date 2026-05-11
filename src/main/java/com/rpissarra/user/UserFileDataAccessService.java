package com.rpissarra.user;


import java.io.*;

public class UserFileDataAccessService implements UserDao {

    private final String filePath;

    public UserFileDataAccessService(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public User[] findAll() {
        try (FileInputStream fis = new FileInputStream(filePath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            int length = ois.readInt();
            User[] users = new User[length];
            int index = 0;
            for (User u : users) {
                users[index++] =(User) ois.readObject();
            }
            return users;
        } catch (FileNotFoundException e){
            System.err.println("File doesn't exist yet.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error reading users from file.");
        }
        return new User[0];
    }
}
