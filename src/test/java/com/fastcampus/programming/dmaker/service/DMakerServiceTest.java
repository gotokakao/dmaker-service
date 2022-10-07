package com.fastcampus.programming.dmaker.service;

import com.fastcampus.programming.dmaker.Exception.DMakerErrorCode;
import com.fastcampus.programming.dmaker.Exception.DMakerException;
import com.fastcampus.programming.dmaker.code.StatusCode;
import com.fastcampus.programming.dmaker.dto.CreateDeveloper;
import com.fastcampus.programming.dmaker.dto.DeveloperDeatilDto;
import com.fastcampus.programming.dmaker.dto.DevelopersDto;
import com.fastcampus.programming.dmaker.entity.Developer;
import com.fastcampus.programming.dmaker.repository.DeveloperRepository;
import com.fastcampus.programming.dmaker.repository.RetiredDeveloperRepository;
import com.fastcampus.programming.dmaker.type.DeveloperLevel;
import com.fastcampus.programming.dmaker.type.DeveloperSkillType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.fastcampus.programming.dmaker.Exception.DMakerErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DMakerServiceTest {

    @Mock
    private DeveloperRepository developerRepository;

    @Mock
    private RetiredDeveloperRepository retiredDeveloperRepository;

    @InjectMocks
    private DMakerService dMakerService;

    private CreateDeveloper.Request getCreateRequest(
            DeveloperLevel developerLevel,
            DeveloperSkillType developerSkillType,
            Integer experienceYear
    ){
        return CreateDeveloper.Request.builder()
                .developerLevel(developerLevel)
                .developerSkillType(developerSkillType)
                .experienceYears(experienceYear)
                .memberId("taemin23.kim")
                .name("taemin")
                .age(36)
                .build();
    }

    private final Developer defaultDeveloper = Developer.builder()
            .developerLevel(DeveloperLevel.JUNGNIOR)
            .developerSkillType(DeveloperSkillType.BACK_END)
            .experienceYears(8)
            .memberId("taemin23.kim")
            .name("taemin")
            .age(36)
            .statusCode(StatusCode.EMPLOYED)
            .build();

    @Test
    void somethingTest(){
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(defaultDeveloper));

        DeveloperDeatilDto actual = dMakerService.getDeveloperDetail("memberId");

        Assertions.assertThat(actual.getDeveloperLevel()).isEqualTo(DeveloperLevel.JUNGNIOR);
        Assertions.assertThat(actual.getName()).isEqualTo("taemin");
    }

    @Test
    void createDeveloperTest_success(){
        //given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.empty());

        given(developerRepository.save(any()))
                .willReturn(defaultDeveloper);

        ArgumentCaptor<Developer> captor = ArgumentCaptor.forClass(Developer.class);

        //when
        dMakerService.createDeveloper(getCreateRequest(DeveloperLevel.JUNGNIOR, DeveloperSkillType.BACK_END,8));

        //then
        verify(developerRepository, times(1))
                .save(captor.capture());

        Developer value = captor.getValue();
        Assertions.assertThat(value.getDeveloperLevel()).isEqualTo(DeveloperLevel.JUNGNIOR);
        assertEquals(8, value.getExperienceYears());
        assertEquals("taemin23.kim", value.getMemberId());
    }

    @Test
    void createDeveloperTest_fail_experienceYear_not_matched(){
        //given
        //when
        DMakerException dMakerException = assertThrows(
                DMakerException.class, () -> dMakerService.createDeveloper(getCreateRequest(DeveloperLevel.SENIOR, DeveloperSkillType.BACK_END, 9))
        );

        assertEquals("개발자 레벨이 개발기간과 부적합 합니다.", dMakerException.getDetailErrorMessage());
        assertEquals(DEVELOPER_LEVEL_NOT_MATCHED, dMakerException.getDMakerErrorCode());
    }

    @Test
    void createDeveloperTest_failed(){
        //given
        CreateDeveloper.Request request = getCreateRequest(DeveloperLevel.JUNGNIOR, DeveloperSkillType.BACK_END, 8);

        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(Developer.builder()
                                .memberId("taemin23.kim")
                        .build()));

        //when

        DMakerException dMakerException = assertThrows(DMakerException.class, () -> dMakerService.createDeveloper(request));
        assertEquals(dMakerException.getDetailErrorMessage(), EXIST_MEMBER_ID.getMessage());
    }

}