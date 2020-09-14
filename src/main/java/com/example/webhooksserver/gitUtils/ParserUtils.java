package com.example.webhooksserver.gitUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.webhooksserver.enums.ParseSplit;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ParserUtils {

    public static boolean isTodo(String parseString) {
        parseString = parseString.trim();
        if (parseString.length() > (3) && parseString.charAt(0) == '+'
                && parseString.indexOf(ParseSplit.TODO.toString()) != -1) {
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

    public static List<String> parseToDos(String content) {
        List<String> taskList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new StringReader(content));
        int enumLength = ParseSplit.TODO.toString().length();
        String todo = ParseSplit.TODO.toString();
        try {
            String currentLine = null;
            while ((currentLine = reader.readLine()) != null) {
                currentLine = currentLine.trim();
                Integer startIndex = -1;
                if (currentLine.length() > (3) && currentLine.charAt(0) == '+'
                        && (startIndex = currentLine.indexOf(todo)) != -1) {
                    taskList.add(currentLine.substring(startIndex + enumLength).trim());
                }
            }
            return taskList;
        } catch (IOException e) {
            log.info(e.getMessage());
            return taskList;
        }

    }

    public static List<LocalDate> parseDate(List<String> parseStrings) {
        List<LocalDate> taskEndDates = new ArrayList<>();
        for (String parseString : parseStrings) {
            taskEndDates.add(ParserUtils.parseDate(parseString));
        }
        return taskEndDates;
    }

    public static HashMap<String, List<Integer>> getTodoLinesWithoutDates(String content) {
        HashMap<String, List<Integer>> separateTodos = new HashMap<>();
        List<Integer> positionNumbers = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new StringReader(content));
        String currentFileName = null;
        String checkFile = null;
        Integer position = -1;
        try {
            String currentLine = null;
            while ((currentLine = reader.readLine()) != null) {
                position += 1;
                if ((checkFile = ParserUtils.getfileName(currentLine)) != null) {
                    if (currentFileName != null)
                        separateTodos.put(currentFileName, positionNumbers);
                    positionNumbers = new ArrayList<>();
                    position = -1;
                    currentFileName = checkFile;
                } else if (currentLine.length() > 0 && currentLine.charAt(0) == '+') {
                    if (ParserUtils.isTodo(currentLine) && !ParserUtils.hasDate(currentLine)) {
                        positionNumbers.add(position);
                    }
                }

            }
            separateTodos.put(currentFileName, positionNumbers);
            return separateTodos;
        } catch (IOException e) {
            log.info(e.getMessage());
            return null;
        }

    }
}