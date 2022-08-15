package com.example.sns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

//TODO : develop
@AllArgsConstructor
@Getter
public class SnsApplicationException extends RuntimeException{

    private ErrorCode errorCode;
    private String messgae;

    public SnsApplicationException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.messgae = null;

    }

    @Override
    public String getMessage() {
        if(messgae == null){
            return errorCode.getMessgage();
        }
        return String.format("%s. %s", errorCode.getMessgage(), messgae);
    }
}
