package com.company.calculator.library;

import java.util.List;

/**
 * Created by Yevhen on 22.04.2016.
 */
public class Numbers {
    public static double parseNumber(String s) {
        return Double.parseDouble(s);
    }

    public static boolean isDoubleOperation(int operandCount, List<String> operandList) {
        // If <operandCount> < 0, th count of operands will not be checked
        boolean result = (operandCount < 0) || (operandList.size() == operandCount);

        if (result) {
            // Try to parse each operand as Double
            try {
                operandList.stream().forEach(Double::parseDouble);
            } catch (NullPointerException | NumberFormatException e) {
                result = false;
            }
        }

        return result;
    }

    public static boolean isIntegerOperation(int operandCount, List<String> operandList) {
        // If <operandCount> < 0, th count of operands will not be checked
        boolean result = (operandCount < 0) || (operandList.size() == operandCount);

        if (result) {
            // Try to parse each operand as Double
            try {
                operandList.stream().forEach(Long::parseLong);
            } catch (NullPointerException | NumberFormatException e) {
                result = false;
            }
        }

        return result;
    }

    private static Class detectConversationClass(String number) {
        Class result;

        try {
            Long.parseLong(number);
            result = Long.class;
        } catch (NullPointerException | NumberFormatException longException) {
            // Try to use Double
            try {
                double doubleValue = Double.parseDouble(number);
                // But again try to use Long
                result = (Math.round(doubleValue) == doubleValue) ? Long.class : Double.class;
            } catch (NullPointerException | NumberFormatException doubleException) {
                result = null;
            }
        }

        return result;
    }

    public static String numberConversation(String number) {
        // Detect possible "minimum" number type of the "whole operation" and store in the field <conversationClass>
        if (detectConversationClass(number) == Long.class) {
            // To avoid to use the fractional part try to re-convert the result as integer (Long) if it is possible
            number = Long.toString((long)Double.parseDouble(number));
        }

        return number;
    }
}
