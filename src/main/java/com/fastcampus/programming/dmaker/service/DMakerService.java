package com.fastcampus.programming.dmaker.service;

import com.fastcampus.programming.dmaker.Exception.DMakerErrorCode;
import com.fastcampus.programming.dmaker.Exception.DMakerException;
import com.fastcampus.programming.dmaker.code.StatusCode;
import com.fastcampus.programming.dmaker.dto.CreateDeveloper;
import com.fastcampus.programming.dmaker.dto.DeveloperDeatilDto;
import com.fastcampus.programming.dmaker.dto.DevelopersDto;
import com.fastcampus.programming.dmaker.dto.EditDeveloper;
import com.fastcampus.programming.dmaker.entity.Developer;
import com.fastcampus.programming.dmaker.entity.RetiredDeveloper;
import com.fastcampus.programming.dmaker.repository.DeveloperRepository;
import com.fastcampus.programming.dmaker.repository.RetiredDeveloperRepository;
import com.fastcampus.programming.dmaker.type.DeveloperLevel;
import com.fastcampus.programming.dmaker.type.DeveloperSkillType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

import static com.fastcampus.programming.dmaker.Exception.DMakerErrorCode.*;

@Service
@RequiredArgsConstructor
public class DMakerService {
    private final DeveloperRepository developerRepository;
    private final RetiredDeveloperRepository retiredDeveloperRepository;

    @Transactional
    public CreateDeveloper.Response createDeveloper(CreateDeveloper.Request request){

        validateCreateDeveloper(request);

        Developer developer = Developer.builder()
                .developerLevel(request.getDeveloperLevel())
                .developerSkillType(request.getDeveloperSkillType())
                .experienceYears(request.getExperienceYears())
                .memberId(request.getMemberId())
                .age(request.getAge())
                .statusCode(StatusCode.EMPLOYED)
                .name(request.getName())
                .build();

        developerRepository.save(developer);

        return CreateDeveloper.Response.fromEntity(developer);
    }

    private void validateCreateDeveloper(CreateDeveloper.Request request) {
        validateDeveloper(request.getDeveloperLevel(), request.getExperienceYears());

        developerRepository.findByMemberId(request.getMemberId())
                .ifPresent((developer -> {
                    throw new DMakerException(EXIST_MEMBER_ID);
                }));
    }

    public List<DevelopersDto> getAllEmployedDevelopers() {
        return developerRepository.findDevelopersByStatusCodeEquals(StatusCode.EMPLOYED)
                .stream().map(DevelopersDto::fromEntity)
                .collect(Collectors.toList());
    }

    public DeveloperDeatilDto getDeveloperDetail(String memerId) {
        return developerRepository.findByMemberId(memerId)
                .map(DeveloperDeatilDto::fromEntity)
                .orElseThrow(() -> new DMakerException(NO_DATA_FOUND));
    }

    @Transactional
    public DeveloperDeatilDto editDeveloper(EditDeveloper.Request request, String memberId) {
        Developer developer = developerRepository.findByMemberId(memberId)
                        .orElseThrow(() -> new DMakerException(NO_DATA_FOUND));

        validateDeveloper(request.getDeveloperLevel(), request.getExperienceYears());

        developer.setDeveloperLevel(request.getDeveloperLevel());
        developer.setDeveloperSkillType(request.getDeveloperSkillType());
        developer.setExperienceYears(request.getExperienceYears());

        return DeveloperDeatilDto.fromEntity(developer);
    }

    private void validateDeveloper(DeveloperLevel developerLevel, Integer experienceYears) {
        if (developerLevel == DeveloperLevel.SENIOR
                && experienceYears < 10) {
            throw new DMakerException(DEVELOPER_LEVEL_NOT_MATCHED);
        }

        if (developerLevel == DeveloperLevel.JUNIOR
                && experienceYears > 5) {
            throw new DMakerException(DEVELOPER_LEVEL_NOT_MATCHED, "주니어 레벨은 5년차 미만만 해당됩니다.");
        }
    }

    @Transactional
    public DeveloperDeatilDto deleteDeveloper(String memberId) {
        Developer developer = developerRepository.findByMemberId(memberId)
                .orElseThrow(() -> new DMakerException(NO_DATA_FOUND));

        developer.setStatusCode(StatusCode.RETIRED);

        RetiredDeveloper retiredDeveloper = RetiredDeveloper.builder()
                .memberId(memberId)
                .name(developer.getName())
                .statusCode(StatusCode.RETIRED)
                .build();

        retiredDeveloperRepository.save(retiredDeveloper);
        return DeveloperDeatilDto.fromEntity(developer);
    }
}
