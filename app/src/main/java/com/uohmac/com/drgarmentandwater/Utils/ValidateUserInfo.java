package com.uohmac.com.drgarmentandwater.Utils;

import android.text.TextUtils;
import android.util.Patterns;

/**
 * Created by Raj on 29/12/15.
 */
public class ValidateUserInfo {

    public static boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isPasswordValid(String password) {
        //TODO change for your own logic
        return password.length() < 8;
    }
}
