package org.library;


import java.util.ArrayList;

public class DbConnector {

    //    ArrayLists to save book data when retrieved
    ArrayList<String> bookAuthorList = new ArrayList<>();
    ArrayList<String> bookTitleList = new ArrayList<>();
    ArrayList<String> bookISBNlist = new ArrayList<>();
    ArrayList<String> bookPublisherList = new ArrayList<>();
    ArrayList<String> bookPageList = new ArrayList<>();
    ArrayList<String> bookPublishedDate = new ArrayList<>();


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
//        retrieving the current user books
        database.getUserBooks(database.getActiveUserID());
        printBorrowedBooks(database);
    }

    public void printBorrowedBooks(Database database) {
        System.out.println("In here");
//        populating the lists from the database instance
        bookAuthorList = database.getBookAuthorList();
        bookTitleList = database.getBookTitleList();
        bookISBNlist = database.getBookISBN();
        bookPublisherList = database.getBookPublisherList();
        bookPageList = database.getBookPublisherList();
        bookPublishedDate = database.getBookPublishedDate();

        int listSize = Math.min(bookAuthorList.size(),
                                Math.min(bookTitleList.size(),
                                        Math.min(bookISBNlist.size(),
                                                Math.min(bookPublishedDate.size(),
                                                        Math.min(bookPublisherList.size(),
                                                                bookPageList.size()
                                )
                                        )
                                                )
                                                        )
        );

//        printing the books
        for (int i = 0; i < listSize; i++) {
            System.out.println(i+1+".\n"
                              + "Title: " + bookAuthorList.get(i)
                              + "Author: " + bookAuthorList.get(i)
                              + "Pages: " + bookPageList.get(i)
                              + "ISBN: " + bookISBNlist.get(i)
                              + "Published Date: " + bookPublishedDate.get(i)
                              + "Publisher: " + bookPublisherList.get(i));
        }
    }
}
