package com.company.calculator.library;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Yevhen on 21.04.2016.
 */
public class SimpleParser implements Parser {
    private static final String IMPOSSIBLE_TO_RECOGNIZE_OPERATION_CODE_PATTERN =
            "It is impossible to recognize available operation code in the expression\n   \"%s\".\n" +
                    "Only these operation codes are known: %s";
    private static final String THERE_ARE_NO_OPERANDS_PATTERN =
            "There are no operands in the expression\n   \"%s\"";
    private static final String MORE_THAN_ONE_AVAILABLE_OPERATION_CODES_ARE_FOUND_PATTERN =
            "More than one available operation codes \"%s\" are found in the expression\n   \"%s\"";

    private static final char DEFAULT_DELIMITER = ' ';

    @Override
    public ParseResult parse(Set<String> operationCodeSet, String inputExpression, char delimiter) {
        // Get operand list as a List
        ArrayList<String> operandList = new ArrayList<>(Arrays.asList(inputExpression.split(Character.toString(delimiter))));
        // In case that <delimiter> is not space character and the leading and(or) trailing whitespace could be presented
        // in the inputExpression
        operandList.forEach(s -> s = s.trim());

        // Firstly, <operator type> is not recognized
        OperatorType operatorType = OperatorType.NOT_RECOGNIZED;
        // Firstly, <operation code> is not recognized
        String operationCode = null;

        // Try to find operation by code in the <operandList>
        final ArrayList<String> operationCodeList =
                operationCodeSet.stream().filter(operandList::contains).
                        collect(Collectors.toCollection(ArrayList<String>::new));
        // Check that there should be only one available operation code
        if (operationCodeList.size() > 1) {
            throw new IllegalArgumentException(String.format(MORE_THAN_ONE_AVAILABLE_OPERATION_CODES_ARE_FOUND_PATTERN,
                    Arrays.toString(operationCodeList.toArray()), inputExpression));
        }
        if (operationCodeList.size() == 1) {
            operationCode = operationCodeList.get(0);
            // Try to detect <operator type>
            if (operandList.indexOf(operationCode) > 0) {
                operatorType = OperatorType.BINARY;
            }
            // Delete <operation code> from operands list
            operandList.remove(operationCode);
        } else {
            // If operation code is not found, try to process operand list to search unary operator
            // (lambda expression cannot be used because of the necessary to change the values of the local valuables
            // such as <operatorType> and <operationCode>)
            for (int i = 0; i < operandList.size(); i++) {
                String operand = operandList.get(i);
                final int operandLength = operand.length();
                for (String operation : operationCodeSet) {
                    int index = operand.indexOf(operation);
                    if (index != - 1) {
                        int operationLength = operation.length();
                        // Unary operator can be present either in the beginning or in the end of operand
                        OperatorType thisOperatorType = (index == 0) ? OperatorType.PREFIX_UNARY :
                                ((index + operationLength) == operandLength) ? OperatorType.POSTFIX_UNARY :
                                        OperatorType.NOT_RECOGNIZED;
                        if (thisOperatorType != OperatorType.NOT_RECOGNIZED) {
                            // Check that only one unary operator should be presented
                            if (operatorType == OperatorType.NOT_RECOGNIZED) {
                                // First unary operator has been found
                                operatorType = thisOperatorType;
                                operationCode = operation;
                                // Modify operand
                                if (operatorType == OperatorType.PREFIX_UNARY) {
                                    operand = operand.substring(operationLength);
                                } else {
                                    operand = operand.substring(0, operandLength-operationLength);
                                }
                                // Save modified operand
                                operandList.set(i, operand);
                            } else {
                                // Only one unary operator should be presented!
                                throw new IllegalArgumentException(String.format(
                                        MORE_THAN_ONE_AVAILABLE_OPERATION_CODES_ARE_FOUND_PATTERN,
                                        String.format("%s, %s", operationCode, operation), inputExpression));
                            }
                        }
                    }
                }
            }
        }

        // Check if operation code is presented
        if (operationCode == null) {
            throw new IllegalArgumentException(String.format(IMPOSSIBLE_TO_RECOGNIZE_OPERATION_CODE_PATTERN,
                    inputExpression, Arrays.toString(operationCodeSet.toArray())));
        }
        // Check if at least one operand is presented
        if (operandList.isEmpty()) {
            throw new IllegalArgumentException(String.format(THERE_ARE_NO_OPERANDS_PATTERN, inputExpression));
        }

        return new SimpleParseResult(operationCode, operatorType, operandList);
    }

    @Override
    public ParseResult parse(Set<String> operationCodeSet, String inputExpression) {
        return parse(operationCodeSet, inputExpression, DEFAULT_DELIMITER) ;
    }
}
