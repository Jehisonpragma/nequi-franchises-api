package co.com.bancolombia.model.exceptionmodel;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCode error;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getLog());
        this.error = errorCode;
    }

}
