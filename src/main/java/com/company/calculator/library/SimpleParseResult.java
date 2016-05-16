package com.company.calculator.library;

import java.util.List;

/**
 * Created by Yevhen on 22.04.2016.
 */
public class SimpleParseResult implements ParseResult {
    private String operationCode;
    private OperatorType operationType;
    private List<String> operandList;

    public SimpleParseResult(String operationCode, OperatorType operationType, List<String> operandList) {
        this.operationCode = operationCode;
        this.operationType = operationType;
        this.operandList = operandList;
    }

    @Override
    public String operationCode() {
        return operationCode;
    }

    @Override
    public OperatorType operatorType() {
        return operationType;
    }

    @Override
    public List<String> operandList() {
        return operandList;
    }
}
