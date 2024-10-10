package org.library;

import javax.xml.crypto.Data;
import java.time.DayOfWeek;

public class DbConnector {

//    Adds userdata
    private void addUserData(String userName, String userLastName, String password,
                         String preference, String school) throws Exception {
        Encryption encryption = new Encryption();
//        Encrypting the password
        String data = encryption.doEncrypt(password);

//        Accessing the Database
        Database database = new Database();
        database.setUserInfo(userName, userLastName, data, preference, school);

    }


// Adds userData to the database
    public void setUserData(String userName, String userLastName, String password,
                            String preference, String school) throws Exception{
        addUserData(userName, userLastName, password, preference, school);
    }

    private boolean checkSecure(String userPass) throws Exception{
        Database db = new Database();
        Encryption encryption = new Encryption();
//        stores secure data
        String data = db.getSecure();

//        decrypts secure data
        String url = encryption.doDecrypt(data);

//        returns result if the password matches
        return matchPasswords(userPass, url);
    }

    public boolean getCheckSecure(String userPass) throws Exception{
        return checkSecure(userPass);
    }

    private boolean matchPasswords(String password, String dbPass) {
        return password.equals(dbPass);
    }

//    returns the result if there is a duplicate userName to Main class
    public boolean duplicateUserName(String userName) {
        Database db = new Database();
       return db.getCheckDuplicateUserName(userName);
    }

//    gets userID
    public int getUserID(String userName) {
        Database db = new Database();
        return db.getUserID(userName);
    }

//Sets the active user ID
    public void setCurrentUserID(int userID) {
        Database db = new Database();
        db.setCurrentUSerID(userID);
    }

//    gets the active userID
    public int getActiveUserID() {
        Database db = new Database();
       return db.getActiveUserID();
    }

    public void replaceOldID(int oldID, int newID) {
        Database db = new Database();
        db.setReplaceOldID(oldID, newID);
    }

    public void getBorrowedBooks(int activeUserID) {
        Database database = new Database();
        database.getUserBooks(database.getActiveUserID());
    }

    public void printBorrowedBooks(Database database) {

    }
}
