package org.library;

import java.net.ConnectException;
import java.sql.*;

public class Database {


    private Connection connect() {
        String url = "jdbc:sqlite:library.db";
        Connection con = null;

        try {
            con = DriverManager.getConnection(url);
        } catch (SQLException e ) {
            System.out.println(e.getMessage());
        }

        return con;
    }

    public void createTables() {

        String bookTable = "CREATE TABLE IF NOT EXISTS books (\n"
                +   "bookID INTEGER PRIMARY KEY AUTOINCREMENT, \n"
                +   "title TEXT NOT NULL, \n"
                +   "author TEXT NOT NULL, \n"
                +   "publisher TEXT NOT NULL, \n"
                +   "isbn TEXT, \n"
                +   "pages TEXT, \n"
                +   "publishedDate TEXT, \n"
                +   "ID INTEGER NOT NULL, \n"
                +   "FOREIGN KEY (ID) REFERENCES users (userID)"
                +   ");";


        String userTable = "CREATE TABLE IF NOT EXISTS users (\n"
                +   "userID INTEGER PRIMARY KEY AUTOINCREMENT, \n"
                +   "userName TEXT NOT NULL, \n"
                +   "userLastName TEXT NOT NULL, \n"
                +   "password TEXT NOT NULL, \n"
                +   "school TEXT NOT NULL, \n"
                +   "preference TEXT NOT NULL"
                +   ");";



        try (Connection connection = this.connect();
            Statement statement = connection.createStatement()) {
            statement.execute(bookTable);
            statement.execute(userTable);
//            System.out.println("Tables created");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void addUserInfo(String userName, String userLastName, String password,
                               String preference, String school) {
        createTables();
        String sql = "INSERT INTO users(userName, userLastName, password, school, preference) VALUES (?,?,?,?,?)";
        try (Connection connection = this.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, userLastName);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, school);
            preparedStatement.setString(5, preference);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void retrievePassword() {
        String sql = "SELECT userName, password FROM users";

        try (Connection connection = this.connect();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                String name = rs.getString("userName");
                String data = rs.getString("password");

                Encryption encryption = new Encryption();
                String pass =  encryption.doDecrypt(data);


            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    setter method to add data to the database
    public void setUserInfo(String userName, String userLastName, String password,
                            String preference, String school) {

        addUserInfo(userName, userLastName, password, preference, school);
    }
}
