package org.library;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) throws Exception{


          boolean run = true;
          while (run) {
              int choice = startApp();
              if (choice == 1) {
                  run = false;
                  login();
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
        return scanner.nextInt();
    }

//    this one logs in to an existing account
    public static void login() throws Exception{
       DbConnector dbConnector = new DbConnector();

        System.out.println("User Name: ");
        String userName = scanner.next();


        System.out.println("Password: ");

        String password = "";
        int attempts = 3;
        while (attempts > 0) {
            password = scanner.next();
            if (dbConnector.getCheckSecure(password)) {
                System.out.println("Logged in");
                break;
            } else {
                System.out.println("Incorrect Password");
                attempts--;
            }
        }

    }


    public static void creatAccount() throws Exception {
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
        String preference = scanner.nextLine();


        login();

//        After taking user data, send the data to the db connector class
        dbConnector.setUserData(name,lastName,password,preference,school);
    }
}