package com.facefive.meetbook;

/**
 * Created by Shahid on 3/22/2018.
 */

public class AppConfig {
    public static final String URL_LOGIN = "http://meetbookapp.com/APIs/login_user.php";

    public static final String URL_SIGNUP = "http://meetbookapp.com/APIs/register_user.php";

    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static final String PASSWORD_PATTERN = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{6,15}$";
    // Password Lentgh is 6-15
    //Password require atleast 1 uppercase and 1 lower case.


}
