package com.fastcampus.programming.dmaker.dto;

import com.fastcampus.programming.dmaker.Exception.DMakerErrorCode;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DmakerErrorResponse {
    private DMakerErrorCode errorCode;
    private String errorMessage;
}
