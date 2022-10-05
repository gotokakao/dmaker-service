package com.fastcampus.programming.dmaker.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DMakerErrorCode {
    DEVELOPER_LEVEL_NOT_MATCHED("개발자 레벨이 개발기간과 부적합 합니다."),
    EXIST_MEMBER_ID("이미 존재하는 memberId 가 있습니다"),
    INTERNAL_SERVER_ERROR("서버에러가 발생하엿습니다."),
    NO_DATA_FOUND("데이터를 찾지 못했습니다."),

    ;

    ;
    private final String message;
}
