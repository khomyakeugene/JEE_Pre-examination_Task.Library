package com.company.calculator.library;

import java.util.*;

/**
 * Created by Yevhen on 22.04.2016.
 */
public class SimpleCalculator implements Calculator {
    private static final String IMPOSSIBLE_TO_RECOGNIZE_OPERATION_SIGNATURE_PATTERN =
            "Operation code has been detected as \"%s\", but it is impossible to recognize available "+
                    "operation signature in the expression\n   \"%s\"";
    private static final String IT_LOOKS_LIKE_THERE_IS_IMBALANCE_OF_LEFT_AND_RIGHT_PARENTHESISES_PATTERN =
            "It looks like there is imbalance of the left and right parenthesises in the expression\n   \"%s\"";

    private static final String LEFT_PARENTHESIS = "(";
    private static final String RIGHT_PARENTHESIS = ")";

    private static final String ADDITION_OPERATION_CODE = "+";
    private static final String SUBTRACT_OPERATION_CODE = "-";

    private Parser parser = new SimpleParser();
    private List<Operation> operationList = new ArrayList<>();

    public SimpleCalculator() {
        initDefaultOperationList();
    }

    @Override
    public void setOperationList(List<Operation> operationList) {
        // Always extend operation list
        this.operationList.addAll(operationList);
    }

    private void initDefaultOperationList() {
        // Addition operation for numbers
        NumberAdditionOperation numberAdditionOperation = new NumberAdditionOperation();
        numberAdditionOperation.setOperationCode(ADDITION_OPERATION_CODE);
        addOperation(numberAdditionOperation);

        // Subtract operation for numbers
        NumberSubtractOperation numberSubtractOperation = new NumberSubtractOperation();
        numberSubtractOperation.setOperationCode(SUBTRACT_OPERATION_CODE);
        addOperation(numberSubtractOperation);
    }

    private String executeElementaryExpression(String expression) {
        String result;

        // Parse input expression: receive operation description in <parseResult> or IllegalArgumentException
        // if <inputExpression> is invalid
        ParseResult parseResult = parser.parse(operationCodeSet(), expression);
        // Search suitable operation, always suppose that if such operation could be found more than one,
        // only the first one should be executed
        Optional<Operation> first = operationList.stream().
                filter(o -> (o.getOperationCode().equals(parseResult.operationCode()) &&
                        o.isThisOperation(expression, parseResult))).findFirst();
        if (first.isPresent()) {
            Operation operation = first.get();
            // Store operands
            operation.setOperands(parseResult.operandList());
            // Execute operation
            result = operation.execute();
        } else {
            throw new IllegalArgumentException(String.format(IMPOSSIBLE_TO_RECOGNIZE_OPERATION_SIGNATURE_PATTERN,
                    parseResult.operationCode(), expression));
        }

        return result;
    }

    @Override
    public void addOperation(Operation operation) {
        operationList.add(operation);
    }


    @Override
    public void setParser(Parser parser) {
        this.parser = parser;
    }

    @Override
    public Set<String> operationCodeSet() {
        HashSet<String> operationCodeSet = new HashSet<>();
        operationList.stream().forEach(o -> operationCodeSet.add(o.getOperationCode()));

        return operationCodeSet;
    }

    private ArrayList<String> separateByTokens(String expressions) {
        // Set of available operation codes as delimiters
        Set<String> operationCodeSet = operationCodeSet();
        // Add left and right parenthesises as a sort of delimiters
        operationCodeSet.add(String.valueOf(LEFT_PARENTHESIS));
        operationCodeSet.add(String.valueOf(RIGHT_PARENTHESIS));
        // Store "one-symbol-length"-operation codes as delimiters
        String delimiters =
                String.join("", (CharSequence[]) operationCodeSet.stream().filter(d -> (d.length() == 1)).
                        toArray(String[]::new));

        ArrayList<String> result = new ArrayList<>();
        StringTokenizer stringTokenizer = new StringTokenizer(expressions);
        while (stringTokenizer.hasMoreElements()) {
            String token = stringTokenizer.nextToken();
            StringTokenizer st = new StringTokenizer(token, delimiters, true);
            while (st.hasMoreElements()) {
                result.add(st.nextToken());
            }
        }

        return result;
    }


    @Override
    public String execute(String expression) {
        // Operation set
        Set<String> operationCodeSet = operationCodeSet();
        // Operands stack
        ArrayDeque<String> operandStack = new ArrayDeque<>();
        // Operator stack
        ArrayDeque<String> operatorStack = new ArrayDeque<>();

        // Separate expression to "tokens"
        ArrayList<String> tokens = separateByTokens(expression);
        for (String token : tokens) {
            if (token.equals(LEFT_PARENTHESIS)) {
                // Left parenthesis
                operatorStack.push(token);
            } else if (token.equals(RIGHT_PARENTHESIS)) {
                // Right parenthesis
                while (!operatorStack.peekFirst().equals(LEFT_PARENTHESIS)) {
                    String operator = operatorStack.pop();
                    String operand2 = operandStack.pop();
                    String operand1 = operandStack.pop();
                    // Construct expression
                    String elementaryExpression = String.format("%s %s %s", operand1, operator, operand2);
                    // Try to calculate expression and push the result onto operand stack
                    String elementaryExpressionResult = executeElementaryExpression(elementaryExpression);
                    operandStack.push(elementaryExpressionResult);
                }
                // Pop the relevant left parenthesis from operator stack and discard it
                String expectedLeftParenthesis = operatorStack.pop();
                // Check of the left and right parenthesises balance
                if (!expectedLeftParenthesis.equals(LEFT_PARENTHESIS)) {
                    throw new IllegalArgumentException(String.format(
                            IT_LOOKS_LIKE_THERE_IS_IMBALANCE_OF_LEFT_AND_RIGHT_PARENTHESISES_PATTERN, expression));
                }
            } else if (operationCodeSet.contains(token)) {
                // Operator

            } else {
                // Operand - just push it onto operand stack
                operandStack.push(token);
            }
        }


        return executeElementaryExpression(expression);
    }
}
