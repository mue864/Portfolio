package org.library;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.sql.*;
import java.util.Base64;

public class Database {

    private int userID;
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

    public int getUserID(String userName) {
        return retrieveCurrentUserID(userName);
    }
    private int retrieveCurrentUserID(String userName) {
        String retrieveQuery = "SELECT userID FROM users WHERE userName = '" + userName + "'";
        try (Connection connection = this.connect();
            PreparedStatement preparedStatement = connection.prepareStatement(retrieveQuery);
            ResultSet resultSet = preparedStatement.executeQuery();) {
            if (resultSet.next()) {
                userID = resultSet.getInt("userID");

                if (resultSet.wasNull()) {
                    userID = 0;
                }
                return userID;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public void setBookStorage(String bookName, String bookAuthor, String isbn, String publisher,
                               String publishedDate, String pageCount) {

        addBookInfo(bookName,bookAuthor,publisher,isbn,publishedDate, pageCount,userID);
    }
//    the id must be fetched from the current user db. but how?
    private void addBookInfo (String bookTitle, String bookAuthor,
                              String bookPublisher, String isbn, String publishedDate, String pages, int userID) {

        String insertInfo = "INSERT INTO books(title, author, publisher, isbn, pages, publishedDate, ID) VALUES(?,?,?,?,?,?,?)";

        try (Connection connection = this.connect();
            PreparedStatement preparedStatement = connection.prepareStatement(insertInfo);) {
            preparedStatement.setString(1,bookTitle);
            preparedStatement.setString(2,bookAuthor);
            preparedStatement.setString(3,bookPublisher);
            preparedStatement.setString(4,isbn);
            preparedStatement.setString(5,pages);
            preparedStatement.setString(5,publishedDate);
            preparedStatement.setInt(6,userID);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
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

        String sqlCreateKeyTable = "CREATE TABLE IF NOT EXISTS storage (\n"
                +   "value TEXT NOT NULL, \n"
                +   "isCreated INTEGER PRIMARY KEY AUTOINCREMENT"
                +   ");";

        try (Connection connection = this.connect();
            PreparedStatement createBookTable = connection.prepareStatement(bookTable);
            PreparedStatement createUserTable = connection.prepareStatement(userTable);
            PreparedStatement createKeyTable = connection.prepareStatement(sqlCreateKeyTable)) {

           createBookTable.execute();
           createUserTable.execute();
           createKeyTable.execute();

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

//    checks if there is a duplicate userName in the database
    public boolean getCheckDuplicateUserName(String userName) {
        return checkDuplicateUserName(userName);
    }

//supposed to store the key in the database
    private void keyStorage(SecretKey key) {
        String sqlInsert = "INSERT INTO storage (value) VALUES (?)";

        try (Connection connection = this.connect();
            PreparedStatement insertStatement = connection.prepareStatement(sqlInsert);) {


//            converts the key from bytes to string
            String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());

            insertStatement.setString(1, encodedKey);
            insertStatement.execute();


        } catch (SQLException e) {
            System.out.println("Here");
            e.printStackTrace();
        }
    }

    /**
     *
     * @param key the secretkey to be stored
     */
    public void getKeyStorage(SecretKey key) {
        keyStorage(key);
    }

//    retrieve the int value to check if a key has been stored yet
    private int retrieveExistsConfirmation() {
        String sql = "SELECT isCreated FROM storage LIMIT 1";

        try (Connection connection = this.connect();
            PreparedStatement retrieveStatement = connection.prepareStatement(sql);
            ResultSet rs = retrieveStatement.executeQuery()) {

            if (rs.next()) {
                int isCreated = rs.getInt("isCreated");

                if (rs.wasNull()) {
                    return 0;
                } else {
                    return isCreated;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public int getExistConfirmation() {
        return retrieveExistsConfirmation();
    }

//    retrieve key from the database
    private SecretKey retrieveKey() {
        String sql = "SELECT value FROM storage LIMIT 1";

        try (Connection connection = this.connect();
             PreparedStatement selectStatement = connection.prepareStatement(sql);
             ResultSet keyResult = selectStatement.executeQuery()) {

            while (keyResult.next()) {
//                Getting the encoded key from db
                String encodedKey = keyResult.getString("value");

//                Decode it back to bytes
                byte[] decodedKey = Base64.getDecoder().decode(encodedKey);

                return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        return null;
    }

    public SecretKey getKey() {
        return retrieveKey();
    }


    private boolean checkDuplicateUserName(String name) {
        createTables();
        String sql = "SELECT userName FROM users";

        try (Connection connection = this.connect();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                String userName = rs.getString("userName");
//              If name exists in the database return true else return false
                if (userName.equals(name)) {
                    return true;
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

//    returns encrypted password
    public String getSecure() {
        return retrievePassword();
    }

    private String retrievePassword() {
        String sql = "SELECT userName, password FROM users";
        String data = "";
        try (Connection connection = this.connect();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                data = rs.getString("password");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return data;
    }

//    setter method to add data to the database
    public void setUserInfo(String userName, String userLastName, String password,
                            String preference, String school) {

        addUserInfo(userName, userLastName, password, preference, school);
    }



}
