package com.example.book.enums;

public enum Theme {
    Literary,
    Adventure,
    Mystery,
    Thriller,
    Horror,
    Historical,
    Romance,
    Western,
    Fantasy;

    public static boolean check(String value){
        try{
            Enum.valueOf(Theme.class, value);
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
