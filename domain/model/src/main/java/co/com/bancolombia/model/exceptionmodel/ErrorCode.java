package co.com.bancolombia.model.exceptionmodel;

import lombok.Getter;

@Getter
public enum ErrorCode {

    E500011("E500-000", "Internal Server Error - Error not identified"),
    E422000("E422-000", "Franchise does not exist"),
    E422001("E422-001", "Branch does not exist"),
    E422002("E422-002", "Product does not exist");

    private final String code;
    private final String log;

    ErrorCode(String code, String log) {
        this.code = code;
        this.log = log;
    }

}
