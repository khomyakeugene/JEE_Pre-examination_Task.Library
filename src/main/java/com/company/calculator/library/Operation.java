package com.company.calculator.library;

import java.util.List;

/**
 * Created by Yevhen on 21.04.2016.
 */
public interface Operation {
    void setOperationCode(String operationCode);

    void setRank(int rank);

    boolean isThisOperation(String inputExpression, ParseResult parseResult);

    void setOperands(List<String> operands);

    String execute();
}

