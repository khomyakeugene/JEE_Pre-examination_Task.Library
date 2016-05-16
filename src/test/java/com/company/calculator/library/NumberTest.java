package com.company.calculator.library;

/**
 * Created by Yevhen on 23.04.2016.
 */
public abstract class NumberTest extends OperationTest {
    private String operand_1;
    private String operand_2;

    protected abstract double calcExpected(double operand_1, double  operand_2);

    @Override
    protected void initOperands() {
        operand_1 = Util.generateNumber();
        operand_2 = Util.generateNumber();

        operands.add(operand_1);
        operands.add(operand_2);
    }

    @Override
    protected String expectedResult() {
        return Numbers.numberConversation(Double.toString(
                calcExpected(Double.parseDouble(operand_1), Double.parseDouble(operand_2))));
    }
}
