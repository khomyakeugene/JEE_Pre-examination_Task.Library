package com.company.calculator.library;

import java.util.List;

/**
 * Created by Yevhen on 21.04.2016.
 */
public interface Operation {
    void setOperationCode(String operationCode);

    String getOperationCode();

    void setRank(int rank);

    int getRank();

    boolean isThisOperation(String inputExpression, ParseResult parseResult);

    OperatorType operatorType();

    void setOperands(List<String> operands);

    String execute();
}

