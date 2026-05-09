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
        } catch (IOException | ClassNotFoundException e) {

        }
        return new User[0];
    }
}
