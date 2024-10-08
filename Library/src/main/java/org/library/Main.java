package org.library;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
//    checks if it is the fist time the user is using the account
    static boolean firstTime = false;
    static String preference = "";
    public static void main(String[] args) throws Exception{
          boolean run = true;
          while (run) {
              int choice = startApp();
              if (choice == 1) {
                  run = false;
                  login(firstTime);
              } else if (choice == 2) {
                  run = false;
                  creatAccount();
              } else {
                  System.out.println("Invalid choice");
              }
          }
    }

//    Shows date and time to the screen
    public static void dateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/,yyyy HH:mm:ss");

        Date date = new Date();
        System.out.println(dateFormat.format(date));
    }

    public static int startApp() {
        System.out.println("Local Library \n\n");
        System.out.println("""
                Please Select:
                1.Login
                2.Create Account
                """);
//        returns the input of the user
        int userChoice = 0;
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input");
                scanner.nextLine();
            }
        }
    }

//    this one logs in to an existing account
    public static void login(boolean isFirstTime) throws Exception{
       DbConnector dbConnector = new DbConnector();

        System.out.println("User Name: ");
        String userName = scanner.next();
        boolean accountExists = dbConnector.duplicateUserName(userName);
//        if the username is not in the database, this means that it does not exist
        while (!accountExists) {
            System.out.println("Account does not exist. Use an existing account.\n\n");
            System.out.println("User Name: ");
            userName = scanner.next();
            accountExists = dbConnector.duplicateUserName(userName);
        }

        System.out.println("Password: ");
        String password = "";
        int attempts = 3;
        while (attempts > 0) {
            password = scanner.next();
            if (dbConnector.getCheckSecure(password)) {
//                if it is the first time for the user, choose picks based on their preference
                if (firstTime) {
                    Book book = new Book();
                    System.out.println("Our picks for you!\n\n");
//                    lists books based on preference
                    book.automaticQuery(preference);
                } else {
                    System.out.println("This is it.. Work in Progress working");
                    bookManagementMenu();
                }

                break;
            } else {
                System.out.println("Incorrect Password");
                attempts--;
            }
        }

    }
//    Gets selection choice
   private static void bookManagementMenu() {
       System.out.println("Library management\n\n");
       System.out.println("""
               Please Select
               1. View Borrowed Books
               2. Borrow Books
               3. Search for Books""");

       int choice = 0;
       boolean loopStatus = true;

       while (loopStatus) {
        try {
            choice = scanner.nextInt();
            if (choice==1) {
                borrowedBooks();
                loopStatus = false;
            } else if (choice == 2) {
                borrowBooks();
                loopStatus = false;
            } else if (choice==3) {
                searchBooks();
                loopStatus = false;
            } else {
                System.out.println("Invalid input");
            }
        } catch (InputMismatchException e) {
            System.out.println("Enter a number");
            scanner.nextLine();
        }
       }

   }
//   manages the books that have been borrowed by the user
   private static void borrowedBooks() {
       System.out.println("\n\n");
       System.out.println("List of the books you borrowed");

   }
//   manages the book borrowing process
   private static void borrowBooks() {
       System.out.println("\n\n");
       System.out.println("Borrow: \n");
       System.out.println("Please note that you are only allowed to borrow 3 books at a time.");
       System.out.println("""
               1. Search by title
               2. Search by author
               3. Search author and title""");

        boolean status = true;
        Book book = new Book();
        int userChoice;
// must account for inputs that are not int
        do {
            try {
                userChoice = scanner.nextInt();
                if (userChoice == 1) {
//            use the preference method to search by title only
                    searchByTitle();
                    status = false;
                } else if (userChoice == 2) {
//            use the authorSearch method to search by author

                    status = false;
                } else if (userChoice == 3) {
//            use the default method for searching

                    status = false;
                } else {
                    System.out.println("Invalid input");
                }
            } catch (InputMismatchException e) {
                System.out.println("Only numbers are required");
                scanner.nextLine();
            }
        } while (status);




   }

    private static void searchByTitle() {
        scanner.nextLine();
        System.out.println("Enter book name: ");
        String title = scanner.nextLine();

        Book book = new Book();
//        Searches book by the title
        book.automaticQuery(title);
    }

    //   Manages the book search process
   private static void searchBooks() {}


    private static void creatAccount() throws Exception {
        DbConnector dbConnector = new DbConnector();

        dateTime();
        System.out.println("Please fill in the following: \n");

        System.out.println("First Name: ");
        String name = scanner.next();

        //      Stores result whether a name is in the database already or not
        boolean result = dbConnector.duplicateUserName(name);

        while (result) {
            System.out.println("User name already exists! Choose another one.");
            name = scanner.next();

//            rechecks the userName again until it is unique
            result = dbConnector.duplicateUserName(name);
        }

        System.out.println("Last Name: ");
        String lastName = scanner.next();

        String password, confirmPassword = "";
        System.out.println("Enter password: ");
        password = scanner.next();

        System.out.println("Confirm Password: ");
        confirmPassword = scanner.next();

//        if password does not match with confirmation, retake the passwords until they match
        while (!password.equals(confirmPassword)) {
            System.out.println("""
                    Passwords do not match
                    Try again.""");

            System.out.println("Password: ");
            password = scanner.next();

            System.out.println("Confirm password: ");
            confirmPassword = scanner.next();
        }
        System.out.println("Passwords match");

        System.out.println("School: ");
        String school = scanner.nextLine();

        System.out.println("Which books do you prefer: ");
        preference = scanner.nextLine();

//        After taking user data, send the data to the db connector class
        dbConnector.setUserData(name,lastName,password,preference,school);

        System.out.println("Account Successfully created. You can now log in.\n");
        firstTime = true;
        login(firstTime);
    }
}