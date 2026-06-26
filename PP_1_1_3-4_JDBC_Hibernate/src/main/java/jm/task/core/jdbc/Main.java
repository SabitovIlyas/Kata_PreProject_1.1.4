package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.sql.SQLException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws SQLException {
        UserService userService = new UserServiceImpl();
        UserCase userCase = new UserCase(userService);
        userCase.start();
    }
}

class UserCase {

    private UserService userService;

    public UserCase(UserService userService) {
        this.userService = userService;
    }

    public void start() {
        try {
            createUsersTable();
            addAllUsersInTableUsers();
            printUsers();
            cleanUsersTable();
            dropUsersTable();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void createUsersTable() {
        userService.createUsersTable();
    }

    private void saveUser(String name, String lastName, byte age) {
        userService.saveUser(name, lastName, age);
        String str = String.format("User с именем — %s добавлен в базу данных", name);
        System.out.println(str);
    }

    private void addAllUsersInTableUsers() {
        saveUser("Иван", "Иванов", (byte) 18);
        saveUser("Пётр", "Петров", (byte) 19);
        saveUser("Василий", "Васечкин", (byte) 20);
        saveUser("Николай", "Николаев", (byte) 21);
    }

    private void cleanUsersTable() {
        userService.cleanUsersTable();
    }

    private List<User> getAllUsersFromTableUsers() {
        return userService.getAllUsers();
    }

    private void printUsers() {
        List<User> users = getAllUsersFromTableUsers();
        for (User user : users) {
            System.out.println(user.toString());
        }
    }

    private void dropUsersTable() {
        userService.dropUsersTable();
    }
}