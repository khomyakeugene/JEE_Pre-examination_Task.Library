package com.company.calculator.library;

/**
 * Created by Yevhen on 20.04.2016.
 */
public interface Calculator {
    /**
     *
     * @param inputExpression is the string which represents some simple algebraic expression, for example, "1+2" or "*,5,6"
     * @return result of calculation of the <b>inputExpression</b>-expression
     * @throws IllegalArgumentException if it is impossible to calculate the expression
     */
    String execute(String inputExpression);

    /**
     *
     * @param operationCode - symbol sequence ("+", "-", "sqrt" etc.) which represents the operationCode
     * @param operation -
     *
     * Add <b>operationCode</b> associated with its <b>operationCode</b> to the list of the operations which can be executed
     */
    void addOperation(String operationCode, Operation operation);

    /**
     *
     * @param parser -
     */
    void setParser(Parser parser);

    /**
     *
     * @return - list of codes of available operations
     */
    String[] operationCodeList();
}
