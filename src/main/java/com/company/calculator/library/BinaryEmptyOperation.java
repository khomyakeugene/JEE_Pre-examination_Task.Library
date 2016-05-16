package com.company.calculator.library;

/**
 * Created by Yevhen on 24.04.2016.
 */
public abstract class BinaryEmptyOperation extends EmptyOperation implements Operation {
    @Override
    public boolean isThisOperation(String inputExpression, ParseResult parseResult) {
        // Check that there are only two operands
        return parseResult.operandList().size() == 2;
    }
}
