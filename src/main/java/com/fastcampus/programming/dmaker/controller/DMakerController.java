package com.fastcampus.programming.dmaker.controller;

import com.fastcampus.programming.dmaker.Exception.DMakerException;
import com.fastcampus.programming.dmaker.dto.*;
import com.fastcampus.programming.dmaker.service.DMakerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DMakerController {

    private final DMakerService dMakerService;

    @GetMapping("/developers")
    public List<DevelopersDto> getAllDevelopers(){
        return dMakerService.getAllEmployedDevelopers();
    }

    @GetMapping("/developer/{memberId}")
    public DeveloperDeatilDto getDeveloperDetail(@PathVariable("memberId") final String memberId){
        return dMakerService.getDeveloperDetail(memberId);
    }


    @PostMapping("/create-developers")
    public CreateDeveloper.Response createDeveloper(@Valid  @RequestBody final CreateDeveloper.Request request){
        log.info("request : {}", request);

        return dMakerService.createDeveloper(request);
    }

    @PutMapping("/developer/{memberId}")
    public DeveloperDeatilDto editDeveloper(@PathVariable("memberId") final String memberId,
                                            @RequestBody  EditDeveloper.Request request){
        return dMakerService.editDeveloper(request, memberId);
    }

    @DeleteMapping("/developer/{memberId}")
    public DeveloperDeatilDto deleteDeveloper(@PathVariable("memberId") final String memberId){
        return dMakerService.deleteDeveloper(memberId);
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(DMakerException.class)
    public DmakerErrorResponse handlerException(DMakerException e, HttpServletRequest req){
        log.error("errorCode: {}, url: {}, message: {}", e.getDMakerErrorCode(), req.getRequestURL(), e.getDetailErrorMessage());

        return DmakerErrorResponse.builder()
                .errorCode(e.getDMakerErrorCode())
                .errorMessage(e.getDetailErrorMessage())
                .build();
    }
}
