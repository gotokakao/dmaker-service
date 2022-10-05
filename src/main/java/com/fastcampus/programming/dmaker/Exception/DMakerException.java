package com.fastcampus.programming.dmaker.Exception;

import lombok.Getter;

@Getter
public class DMakerException extends RuntimeException{
    private DMakerErrorCode dMakerErrorCode;
    private String detailErrorMessage;

    public DMakerException(DMakerErrorCode errorCode){
        super(errorCode.getMessage());
        this.dMakerErrorCode = errorCode;
        this.detailErrorMessage = errorCode.getMessage();
    }

    public DMakerException(DMakerErrorCode errorCode, String detailErrorMessage){
        super(detailErrorMessage);
        this.dMakerErrorCode = errorCode;
        this.detailErrorMessage = detailErrorMessage;
    }
}
