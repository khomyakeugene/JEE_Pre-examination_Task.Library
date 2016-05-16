package com.company.calculator.library;

/**
 * Created by Yevhen on 23.04.2016.
 */
public abstract class IntegerOperation extends NumberOperation implements Operation {
    @Override
    public boolean isThisOperation(String inputExpression, ParseResult parseResult) {
        return Numbers.isIntegerOperation(getExpectedOperandCount(), parseResult.operandList());
    }

    public Long getIntegerOperand(int index) {
        return Long.parseLong(getOperand(index));
    }
}
