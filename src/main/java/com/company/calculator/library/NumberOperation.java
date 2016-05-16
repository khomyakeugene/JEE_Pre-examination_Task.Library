package com.company.calculator.library;

/**
 * Created by Yevhen on 22.04.2016.
 */
public abstract class NumberOperation extends EmptyOperation implements Operation {
    @Override
    public boolean isThisOperation(String inputExpression, ParseResult parseResult) {
        return Numbers.isDoubleOperation(getExpectedOperandCount(), parseResult.operandList());
    }

    // Should be overridden in the descendant class
    protected abstract String calculate();

    @Override
    public final String execute() {
        return numberConversation(calculate());
    }

    public Double getDoubleOperand(int index) {
        return Double.parseDouble(getOperand(index));
    }

    private String numberConversation(String number) {
        return Numbers.numberConversation(number);
    }
}
