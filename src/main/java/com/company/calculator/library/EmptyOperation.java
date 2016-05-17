package com.company.calculator.library;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yevhen on 22.04.2016.
 */
public abstract class EmptyOperation implements Operation {
    private String operationCode;
    private int rank;
    private int expectedOperandCount = -1;
    private ArrayList<String> operandList = new ArrayList<>();

    @Override
    public abstract boolean isThisOperation(String inputExpression, ParseResult parseResult);

    @Override
    public abstract String execute();

    @Override
    public String getOperationCode() {
        return operationCode;
    }

    @Override
    public void setOperationCode(String operationCode) {
        this.operationCode = operationCode;
    }

    @Override
    public int getRank() {
        return rank;
    }

    @Override
    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public void setOperands(List<String> operands) {
        operandList.clear();
        operandList.addAll(operands);
    }

    protected int getExpectedOperandCount() {
        return expectedOperandCount;
    }

    protected ArrayList<String> getOperandList() {
        return operandList;
    }

    protected String getOperand(int index) {
        return operandList.get(index);
    }

    protected void setExpectedOperandCount(int expectedOperandCount) {
        this.expectedOperandCount = expectedOperandCount;
    }
}
