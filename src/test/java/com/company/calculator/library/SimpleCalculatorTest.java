package com.company.calculator.library;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Yevhen on 23.04.2016.
 */
public class SimpleCalculatorTest extends ExpressionTest {
    private static SimpleCalculator simpleCalculator = new SimpleCalculator();

    private String getExpectedResult() {
        Double result = Double.MIN_VALUE;

        switch (operationCode) {
            case ADDITION_OPERATION_CODE:
                result = Double.parseDouble(operand_1) + Double.parseDouble(operand_2);
                break;

            case SUBTRACT_OPERATION_CODE:
                result = Double.parseDouble(operand_1) - Double.parseDouble(operand_2);
                break;
        }

        return Numbers.numberConversation(result.toString());
    }

    private void executeTest(String expression) throws Exception {
        String expected = getExpectedResult();

        assertEquals(expected, simpleCalculator.execute(expression));
    }

    @Test(timeout = 1000)
    public void executeValidExpressionTest() throws Exception {
        executeTest(validBinaryOperationExpression);
    }

    @Test(timeout = 1000, expected = IllegalArgumentException.class)
    public void executeInvalidExpressionTest() throws Exception {
        executeTest(invalidBinaryOperationExpression);
    }
}