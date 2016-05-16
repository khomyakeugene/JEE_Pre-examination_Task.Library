package com.company.calculator.library;

/**
 * Created by Yevhen on 22.04.2016.
 */
public abstract class BinaryNumberOperation extends NumberOperation implements Operation {
    private final static int OPERANDS_COUNT = 2;

    public BinaryNumberOperation() {
        setExpectedOperandCount(OPERANDS_COUNT);
    }
}
