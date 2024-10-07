package org.library;

import javax.xml.crypto.Data;

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

    public void setUserData(String userName, String userLastName, String password,
                            String preference, String school) throws Exception{
        addUserData(userName, userLastName, password, preference, school);
    }

}
