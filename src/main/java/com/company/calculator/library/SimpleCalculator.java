package com.company.calculator.library;

import java.util.*;

/**
 * Created by Yevhen on 22.04.2016.
 */
public class SimpleCalculator implements Calculator {
    private static final String IMPOSSIBLE_TO_RECOGNIZE_OPERATION_SIGNATURE_PATTERN =
            "Operation code has been detected as \"%s\", but it is impossible to recognize available " +
                    "operation signature in the expression\n   \"%s\"";
    private static final String IT_LOOKS_LIKE_THERE_IS_IMBALANCE_OF_LEFT_AND_RIGHT_PARENTHESISES_PATTERN =
            "It looks like there is imbalance of the left and right parenthesises in the expression\n   \"%s\"";
    private static final String IMPOSSIBLE_TO_DETECT_OPERATION_BY_CODE_PATTERN =
            "It is impossible to detect operation by code \"%s\" in the expression\n   \"%s\"";
    private static final String IMPOSSIBLE_TO_CALCULATE_EXPRESSION_PATTERN =
            "It is impossible to calculate expression\n   \"%s\"";

    private static final String LEFT_PARENTHESIS = "(";
    private static final String RIGHT_PARENTHESIS = ")";

    private static final String ADDITION_OPERATION_CODE = "+";
    private static final String SUBTRACT_OPERATION_CODE = "-";
    private static final String MULTIPLICATION_OPERATION_CODE = "*";
    private static final String DIVIDING_OPERATION_CODE = "/";

    private static final int ADDITION_AND_SUBTRACT_RANK = 0;
    private static final int MULTIPLICATION_AND_DIVIDING_RANK = 100;

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
        numberAdditionOperation.setRank(ADDITION_AND_SUBTRACT_RANK);
        addOperation(numberAdditionOperation);

        // Subtract operation for numbers
        NumberSubtractOperation numberSubtractOperation = new NumberSubtractOperation();
        numberSubtractOperation.setOperationCode(SUBTRACT_OPERATION_CODE);
        numberSubtractOperation.setRank(ADDITION_AND_SUBTRACT_RANK);
        addOperation(numberSubtractOperation);

        // Multiplication operation for numbers
        NumberMultiplicationOperation numberMultiplicationOperation = new NumberMultiplicationOperation();
        numberMultiplicationOperation.setOperationCode(MULTIPLICATION_OPERATION_CODE);
        numberMultiplicationOperation.setRank(MULTIPLICATION_AND_DIVIDING_RANK);
        addOperation(numberMultiplicationOperation);

        // Dividing operation for numbers
        NumberDividingOperation numberDividingOperation = new NumberDividingOperation();
        numberDividingOperation.setOperationCode(DIVIDING_OPERATION_CODE);
        numberDividingOperation.setRank(MULTIPLICATION_AND_DIVIDING_RANK);
        addOperation(numberDividingOperation);
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
        operationCodeSet.add(LEFT_PARENTHESIS);
        operationCodeSet.add(RIGHT_PARENTHESIS);
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

    private Operation getOperationByOperationCode(String operationCode, boolean throwException, String expression) {
        Operation result = null;

        Optional<Operation> operationOptional =
                operationList.stream().filter(o -> o.getOperationCode().equals(operationCode)).findFirst();
        if (operationOptional.isPresent()) {
            result = operationOptional.get();
        } else {
            if (throwException) {
                throw new IllegalArgumentException(String.format(IMPOSSIBLE_TO_DETECT_OPERATION_BY_CODE_PATTERN,
                        operationCode, expression));
            }
        }

        return result;
    }

    private Operation getOperationByOperationCode(String operationCode) {
        return getOperationByOperationCode(operationCode, false, null);
    }

    private void leftRightParenthesisesImbalanceError(String expression) {
        throw new IllegalArgumentException(String.format(
                IT_LOOKS_LIKE_THERE_IS_IMBALANCE_OF_LEFT_AND_RIGHT_PARENTHESISES_PATTERN, expression));
    }

    private void impossibleToCalculateExpressionError(String expression) {
        throw new IllegalArgumentException(String.format(IMPOSSIBLE_TO_CALCULATE_EXPRESSION_PATTERN, expression));
    }

    private void calculateElementaryOperation(ArrayDeque<String> operandStack, ArrayDeque<String> operationStack,
                                              String expression) {
        String elementaryExpression;

        // Data integrity check
        if (operationStack.isEmpty()) {
            impossibleToCalculateExpressionError(expression);
        }
        String operationCode = operationStack.pop();
        // <operationCode> could be parenthesis only if there is an imbalance of left and right parenthesises
        if (operationCode.equals(LEFT_PARENTHESIS) || operationCode.equals(RIGHT_PARENTHESIS)) {
            leftRightParenthesisesImbalanceError(expression);
        }
        Operation operation = getOperationByOperationCode(operationCode, true, expression);

        // Operands
        // Data integrity check
        if (operandStack.isEmpty()) {
            impossibleToCalculateExpressionError(expression);
        }
        String operand2 = operandStack.pop();
        // Unary or binary operationCode?
        OperatorType operatorType = operation.operatorType();
        if (operatorType == OperatorType.PREFIX_UNARY || operatorType == OperatorType.POSTFIX_UNARY) {
            // Unary operation
            if (operatorType == OperatorType.PREFIX_UNARY ) {
                elementaryExpression = String.format("%s %s", operationCode, operand2);
            } else {
                elementaryExpression = String.format("%s%s", operand2, operationCode);
            }
        } else {
            // Binary operation
            // Data integrity check
            if (operandStack.isEmpty()) {
                impossibleToCalculateExpressionError(expression);
            }
            String operand1 = operandStack.pop();
            // Construct expression
            elementaryExpression = String.format("%s %s %s", operand1, operationCode, operand2);
        }

        // Try to calculate expression and push the result onto operand stack
        operandStack.push(executeElementaryExpression(elementaryExpression));
    }

    @Override
    public String execute(String expression) {
        // Operation set
        Set<String> operationCodeSet = operationCodeSet();
        // Operands stack
        ArrayDeque<String> operandStack = new ArrayDeque<>();
        // Operation stack
        ArrayDeque<String> operationStack = new ArrayDeque<>();

        // Separate expression to "tokens"
        ArrayList<String> tokens = separateByTokens(expression);
        for (String token : tokens) {
            if (token.equals(LEFT_PARENTHESIS)) {
                // Left parenthesis
                operationStack.push(token);
            } else if (token.equals(RIGHT_PARENTHESIS)) {
                // Right parenthesis
                while (!operationStack.isEmpty() && !operationStack.peekFirst().equals(LEFT_PARENTHESIS)) {
                    calculateElementaryOperation(operandStack, operationStack, expression);
                }
                // Check the left and right parenthesises balance
                if (operationStack.isEmpty()) {
                    leftRightParenthesisesImbalanceError(expression);
                } else {
                    // Pop the relevant left parenthesis from operation stack and discard it
                    operationStack.pop();
                }
            } else if (operationCodeSet.contains(token)) {
                // Operation
                Operation operation = getOperationByOperationCode(token, true, expression);
                int operationRank = operation.getRank();
                while (!operationStack.isEmpty()) {
                    Operation peekOperation = getOperationByOperationCode(operationStack.peekFirst());
                    // Parenthesises can also be presented in the <operationStack> ...
                    if (peekOperation != null) {
                        if (peekOperation.getRank() >= operationRank) {
                            calculateElementaryOperation(operandStack, operationStack, expression);
                        } else {
                            break;
                        }
                    } else {
                        break;
                    }
                }
                // Push this operation onto operation stack
                operationStack.push(token);
            } else {
                // Operand - just push it onto operand stack
                operandStack.push(token);
            }
        }

        // While the operation stack is not empty ...
        while (!operationStack.isEmpty()) {
            calculateElementaryOperation(operandStack, operationStack, expression);
        }

        // Pop from operand stack the result of the whole expression
        String result = operandStack.pop();
        // <operandStack> could be not empty if some "tokens" has not been recognised as operands or operations
        if (!operandStack.isEmpty()) {
            impossibleToCalculateExpressionError(expression);
        }

        return result;
    }
}
