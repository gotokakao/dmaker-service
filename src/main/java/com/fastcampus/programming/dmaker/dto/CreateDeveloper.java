package com.fastcampus.programming.dmaker.dto;

import com.fastcampus.programming.dmaker.entity.Developer;
import com.fastcampus.programming.dmaker.type.DeveloperLevel;
import com.fastcampus.programming.dmaker.type.DeveloperSkillType;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateDeveloper {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class Request{
        @NotNull
        private DeveloperLevel developerLevel;

        @NotNull
        private DeveloperSkillType developerSkillType;

        @NotNull
        @Min(0)
        @Max(30)
        private Integer experienceYears;

        @NotNull(message = "Please fill memberId")
        @Size(max = 50, message = "memberId size must 50")
        private String memberId;

        @NotNull
        @Size(max = 50, message = "name size must 50")
        private String name;
        private Integer age;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response{
        private DeveloperLevel developerLevel;
        private DeveloperSkillType developerSkillType;
        private String memberId;
        private String name;

        public static Response fromEntity(@NotNull Developer developer){
            return  Response.builder()
                    .developerLevel(developer.getDeveloperLevel())
                    .developerSkillType(developer.getDeveloperSkillType())
                    .memberId(developer.getMemberId())
                    .name(developer.getName())
                    .build();

        }
    }
}
