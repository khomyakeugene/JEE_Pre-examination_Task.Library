package com.company.calculator.library;

/**
 * Created by Yevhen on 22.04.2016.
 */
public class NumberAdditionOperation extends BinaryNumberOperation implements Operation {
    @Override
    protected String calculate() {
        return Double.toString(getDoubleOperand(0) + getDoubleOperand(1));
    }
}
