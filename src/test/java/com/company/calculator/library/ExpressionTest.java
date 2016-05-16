package com.company.calculator.library;

import org.junit.Before;

/**
 * Created by Yevhen on 23.04.2016.
 */
class ExpressionTest {
    private static final String INVALID_OPERATION_CODE = "^^^";

    static final String ADDITION_OPERATION_CODE = "+";
    static final String SUBTRACT_OPERATION_CODE = "-";
    private static final String EXPRESSION_PATTERN = "%s %s %s";

    String operand_1;
    String operand_2;
    String operationCode;
    String validBinaryOperationExpression;
    String invalidBinaryOperationExpression;

    private void generateTestData() {
        operand_1 = Util.generateNumber();
        operand_2 = Util.generateNumber();
        operationCode = Util.getRandomBoolean() ? ADDITION_OPERATION_CODE : SUBTRACT_OPERATION_CODE;

        validBinaryOperationExpression = String.format(EXPRESSION_PATTERN, operand_1, operationCode, operand_2);
        invalidBinaryOperationExpression = String.format(EXPRESSION_PATTERN, operand_1, INVALID_OPERATION_CODE, operand_2);
    }

    @Before
    public void setUp() throws Exception {
        generateTestData();
    }
}
