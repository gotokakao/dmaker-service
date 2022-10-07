package com.fastcampus.programming.dmaker.type;

import com.fastcampus.programming.dmaker.Exception.DMakerException;
import com.fastcampus.programming.dmaker.constant.DmakerConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.function.Function;

import static com.fastcampus.programming.dmaker.Exception.DMakerErrorCode.DEVELOPER_LEVEL_NOT_MATCHED;
import static com.fastcampus.programming.dmaker.constant.DmakerConstant.*;

@AllArgsConstructor
@Getter
public enum DeveloperLevel {
    New("신입 개발자", years -> years == 0),
    JUNIOR("주니어 개발자",years -> years <= MAX_JUNIOR_EXPERIENCE_YEAR),
    JUNGNIOR("중니어 개발자", years -> years > MAX_JUNIOR_EXPERIENCE_YEAR &&
            years < MIN_SENIOR_EXPERIENCE_YEAR),
    SENIOR("시니어 개발자", years -> years >= MIN_SENIOR_EXPERIENCE_YEAR),
    ;

    private final String description;
    private final Function<Integer, Boolean> validateFunction;

    public void validateExperienceYear(Integer years){
        if(!validateFunction.apply(years)){
            throw new DMakerException(DEVELOPER_LEVEL_NOT_MATCHED);
        }
    }
}
