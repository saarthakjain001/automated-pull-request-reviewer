package com.example.webhooksserver.gitUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.webhooksserver.gitUtils.enums.ToDoEnum;

public class ParserUtils {

    public static boolean isTodo(String parseString) {
        parseString = parseString.trim();
        Integer startIndex = -1;
        if (parseString.length() > (3) && parseString.charAt(0) == '+'
                && (startIndex = parseString.indexOf(ToDoEnum.TODO.toString())) != -1) {
            return true;
        }
        return false;
    }

    public static boolean hasDate(String parseString) {
        String regex = "(\\d{1,2}(/|-)\\d{1,2}(/|-)\\d{4})";
        Pattern datePattern = Pattern.compile(regex);
        parseString.trim();
        Matcher m = datePattern.matcher(parseString);
        if (m.find() == true) {
            return true;

        } else {
            return false;
        }
    }

    public static LocalDate parseDate(String parseString) {
        LocalDate date;
        String regex = "(\\d{1,2}(/|-)\\d{1,2}(/|-)\\d{4})";
        List<DateTimeFormatter> formatter = new ArrayList<>();
        formatter.add(DateTimeFormatter.ofPattern("dd-MM-uuuu"));
        formatter.add(DateTimeFormatter.ofPattern("dd/MM/uuuu"));
        formatter.add(DateTimeFormatter.ofPattern("d/M/uuuu"));
        formatter.add(DateTimeFormatter.ofPattern("d-M-uuuu"));
        formatter.add(DateTimeFormatter.ofPattern("dd/M/uuuu"));
        formatter.add(DateTimeFormatter.ofPattern("dd-M-uuuu"));
        formatter.add(DateTimeFormatter.ofPattern("d/MM/uuuu"));
        formatter.add(DateTimeFormatter.ofPattern("d-MM-uuuu"));
        Pattern datePattern = Pattern.compile(regex);
        parseString.trim();
        Matcher m = datePattern.matcher(parseString);
        if (m.find() == true) {
            for (DateTimeFormatter formats : formatter) {
                try {
                    date = LocalDate.parse(m.group(1), formats);
                    return date;
                } catch (Exception e) {
                    continue;
                }
            }
            return null;
        } else {
            return null;
        }
    }

    public static String getfileName(String parseString) {
        if (parseString.length() > (3) && parseString.charAt(0) == '+' && parseString.charAt(1) == '+'
                && parseString.charAt(2) == '+' && parseString.charAt(4) == 'b') {
            return parseString.substring(6);
        }
        return null;
    }

    public static String removeDate(String todoString) {
        String regex = "(\\d{1,2}(/|-)\\d{1,2}(/|-)\\d{4})";
        Pattern datePattern = Pattern.compile(regex);
        Matcher m = datePattern.matcher(todoString);
        if (m.find())
            return todoString.substring(m.end());
        return todoString;
    }
}