package com.company.calculator.library;

/**
 * Created by Yevhen on 23.04.2016.
 */

public abstract class UnaryNumberOperation extends NumberOperation implements Operation {
    private final static int OPERANDS_COUNT = 1;

    public UnaryNumberOperation() {
        setExpectedOperandCount(OPERANDS_COUNT);
    }
}
