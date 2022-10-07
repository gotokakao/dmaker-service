package com.fastcampus.programming.dmaker.controller;

import com.fastcampus.programming.dmaker.dto.DevelopersDto;
import com.fastcampus.programming.dmaker.service.DMakerService;
import com.fastcampus.programming.dmaker.type.DeveloperLevel;
import com.fastcampus.programming.dmaker.type.DeveloperSkillType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DMakerController.class)
class DMakerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DMakerService dMakerService;

    protected MediaType contentType =
            new MediaType(MediaType.APPLICATION_JSON.getType(),
                    MediaType.APPLICATION_JSON.getSubtype(),
                    StandardCharsets.UTF_8);

    @Test
    void getAllDeveloperTest() throws Exception {
        DevelopersDto juniorDevelopersDto = DevelopersDto.builder()
                .developerSkillType(DeveloperSkillType.BACK_END)
                .developerLevel(DeveloperLevel.JUNIOR)
                .memberId("taemin23.kim")
                .build();

        DevelopersDto seniorDeveloperDto = DevelopersDto.builder()
                .developerSkillType(DeveloperSkillType.FRONT_END)
                .developerLevel(DeveloperLevel.SENIOR)
                .memberId("mickey")
                .build();

        given(dMakerService.getAllEmployedDevelopers())
                .willReturn(Arrays.asList(juniorDevelopersDto, seniorDeveloperDto));

        ResultActions actual = mockMvc.perform(get("/developers")
                .contentType(contentType))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].developerSkillType", is(DeveloperSkillType.BACK_END.name())))
                .andExpect(jsonPath("$.[0].developerLevel", is(DeveloperLevel.JUNIOR.name())))
                .andExpect(jsonPath("$.[1].developerSkillType", is(DeveloperSkillType.FRONT_END.name())))
                .andExpect(jsonPath("$.[1].developerLevel", is(DeveloperLevel.SENIOR.name())));

    }

}