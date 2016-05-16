package com.company.calculator.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Created by Yevhen on 22.04.2016.
 */
public class SimpleCalculator implements Calculator {
    private static final String IMPOSSIBLE_TO_RECOGNIZE_OPERATION_SIGNATURE_PATTERN =
            "Operation code has been detected as \"%s\", but it is impossible to recognize available "+
                    "operation signature in the expression\n   \"%s\"";

    private static final String ADDITION_OPERATION_CODE = "+";
    private static final String SUBTRACT_OPERATION_CODE = "-";

    private HashMap<String, List<Operation>> operationMap = new HashMap<>();
    private Parser parser = new SimpleParser();

    public SimpleCalculator() {
        initDefaultOperationList();
    }

    private void initDefaultOperationList() {
        // Addition operation for numbers
        addOperation(ADDITION_OPERATION_CODE, new NumberAdditionOperation());
        // Subtract operation for numbers
        addOperation(SUBTRACT_OPERATION_CODE, new NumberSubtractOperation());
    }

    @Override
    public String execute(String inputExpression) {
        String result;

        // Parse input expression: receive operation description in <parseResult> or IllegalArgumentException
        // if <inputExpression> is invalid
        ParseResult parseResult = parser.parse(operationMap.keySet(), inputExpression);
        // Search suitable operation, always suppose that if such operation could be found more than one,
        // only the first one should be executed
        Optional<Operation> first = operationMap.get(parseResult.operationCode()).
                stream().filter(o -> o.isThisOperation(inputExpression, parseResult)).findFirst();
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
    public void addOperation(String operationCode, Operation operation) {
        // Search operation by <operationCode>
        List<Operation> operationList = operationMap.get(operationCode);
        // If yet not presented than create
        if (operationList == null) {
            operationList = new ArrayList<>();
        }

        // Add operation
        operationList.add(operation);
        // Store new operation list
        operationMap.put(operationCode, operationList);
    }

    @Override
    public void setParser(Parser parser) {
        this.parser = parser;
    }

    @Override
    public String[] operationCodeList() {
        return operationMap.keySet().toArray(new String[operationMap.keySet().size()]);
    }
}
