package com.company.calculator.library;

/**
 * Created by Yevhen on 23.04.2016.
 */
public abstract class UnaryIntegerOperation extends IntegerOperation implements Operation {
    private final static int OPERANDS_COUNT = 1;

    public UnaryIntegerOperation() {
        setExpectedOperandCount(OPERANDS_COUNT);
    }
}
