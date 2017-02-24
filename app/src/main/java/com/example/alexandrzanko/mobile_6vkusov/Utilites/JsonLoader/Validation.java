package com.example.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alexandrzanko on 2/23/17.
 */

public class Validation {

    public static boolean email(String email){
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPattern.matcher(email);
        return matcher.find();
    }

    public static boolean minLength(String password, int length){
        return password.length() >= length ? true : false;
    }

    public static boolean maxLength(String password, int length){
        return password.length() <= length ? true : false;
    }

    public static boolean nameLiterals(String name){
        Pattern namePattern = Pattern.compile("^[a-zA-ZА-Яа-я]+$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = namePattern.matcher(name);
        return  matcher.find();
    }

    public static boolean nameLiteralsAndNumbers(String name){
        Pattern namePattern = Pattern.compile("^[a-zA-ZА-Яа-я0-9]+$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = namePattern.matcher(name);
        return  matcher.find();
    }

}
