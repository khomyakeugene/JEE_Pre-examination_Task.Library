package com.company.calculator.library;

import java.util.*;

/**
 * Created by Yevhen on 22.04.2016.
 */
public class SimpleCalculator implements Calculator {
    private static final String IMPOSSIBLE_TO_RECOGNIZE_OPERATION_SIGNATURE_PATTERN =
            "Operation code has been detected as \"%s\", but it is impossible to recognize available "+
                    "operation signature in the expression\n   \"%s\"";

    private static final String ADDITION_OPERATION_CODE = "+";
    private static final String SUBTRACT_OPERATION_CODE = "-";

    private Parser parser = new SimpleParser();

    List<Operation> operationList;

    public SimpleCalculator() {
        initDefaultOperationList();
    }

    @Override
    public void setOperationList(List<Operation> operationList) {
        if (this.operationList == null) {
            this.operationList = operationList;
        } else {
            this.operationList.addAll(operationList);
        }
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

    @Override
    public String execute(String inputExpression) {
        String result;

        // Parse input expression: receive operation description in <parseResult> or IllegalArgumentException
        // if <inputExpression> is invalid
        ParseResult parseResult = parser.parse(operationCodeSet(), inputExpression);
        // Search suitable operation, always suppose that if such operation could be found more than one,
        // only the first one should be executed
        Optional<Operation> first = operationList.stream().
                filter(o -> (o.getOperationCode().equals(parseResult.operationCode()) &&
                                o.isThisOperation(inputExpression, parseResult))).findFirst();
        if (first.isPresent()) {
            Operation operation = first.get();
            // Store operands
            operation.setOperands(parseResult.operandList());
            // Execute operation
            result = operation.execute();
        } else {
            throw new IllegalArgumentException(String.format(IMPOSSIBLE_TO_RECOGNIZE_OPERATION_SIGNATURE_PATTERN,
                    parseResult.operationCode(), inputExpression));
        }

        return result;
    }

    @Override
    public void addOperation(Operation operation) {
        // If yet not presented than create
        if (operationList == null) {
            operationList = new ArrayList<>();
        }
        // Add operation
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
}
