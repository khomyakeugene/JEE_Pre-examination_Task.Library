package com.company.calculator.library;

import java.util.List;

/**
 * Created by Yevhen on 21.04.2016.
 */
public interface ParseResult {
    String operationCode();

    OperatorType operatorType();

    List<String> operandList();
}
