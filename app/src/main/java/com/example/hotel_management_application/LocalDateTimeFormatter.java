package com.example.hotel_management_application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeFormatter {

    static DateTimeFormatter dateAndTimeObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");


    static DateTimeFormatter dateObj = DateTimeFormatter.ofPattern("d/M/yyyy");


    public static String formatDateAndTime (LocalDateTime dateAndTime) {
        if(dateAndTime != null)
            return dateAndTime.format(dateAndTimeObj);
        return null;
    }

    public static String formatDate (LocalDate localDate) {
        if(localDate != null)
            return localDate.format(dateObj);
        return null;
    }

    public static LocalDate makeDateFromString (String date) {
        String[] split = date.split("/");
        int daysLength = split[0].length();
        int monthsLength = split[1].length();
        String pattern = "";

        if(daysLength == 1)
            pattern += "d/";
        else pattern += "dd/";

        if(monthsLength == 1)
            pattern += "M/";
        else pattern += "MM/";

        pattern += "yyyy";

        return LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDateTime makeDateTimeFromString(String dateAndTime) {
        return LocalDateTime.parse(dateAndTime, dateAndTimeObj);
    }

}