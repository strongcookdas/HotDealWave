package com.sparta.hotdeal.company.presentation.controller;

import com.sparta.hotdeal.company.application.service.CompanyService;
import com.sparta.hotdeal.company.domain.entity.CompanyStatusEnum;
import com.sparta.hotdeal.company.application.dtos.ReqPatchCompanyByIdDto;
import com.sparta.hotdeal.company.application.dtos.ReqPatchCompanyByIdStatusDto;
import com.sparta.hotdeal.company.application.dtos.ReqPostCompanyDto;
import com.sparta.hotdeal.company.application.dtos.ResGetCompanyByIdDto;
import com.sparta.hotdeal.company.application.dtos.ResponseDto;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<Void> createCompany(@RequestBody ReqPostCompanyDto reqPostCompanyDto) {
        companyService.createCompany(reqPostCompanyDto);
        return ResponseDto.of("업체가 신청되었습니다.", null);
    }

    @PatchMapping("/{companyId}")
    public ResponseDto<Void> updateCompany(@PathVariable UUID companyId,
                                           @RequestBody ReqPatchCompanyByIdDto reqPatchCompanyByIdDto) {
        return ResponseDto.of("업체가 수정되었습니다.", null);
    }

    @PatchMapping("/{companyId}/approve")
    public ResponseDto<Void> updateCompanyStatus(@PathVariable UUID companyId,
                                                 @RequestBody ReqPatchCompanyByIdStatusDto reqPatchCompanyByIdStatusDto) {
        return ResponseDto.of("업체가 승인되었습니다.", null);
    }

    @GetMapping("/{companyId}")
    public ResponseDto<ResGetCompanyByIdDto> getCompanyById(@PathVariable UUID companyId) {
        ResGetCompanyByIdDto resGetCompanyByIdDto = ResGetCompanyByIdDto.builder()
                .companyEmail("test1@test.com")
                .companyPhoneNumber(12345678L)
                .brandName("testBrandName")
                .businessRegistrationNumber(12345678L)
                .managerId(UUID.randomUUID())
                .status(CompanyStatusEnum.PENDING)
                .build();
        return ResponseDto.of("업체가 조회되었습니다.", resGetCompanyByIdDto);
    }

    @GetMapping
    public ResponseDto<List<ResGetCompanyByIdDto>> getCompanyList() {
        ResGetCompanyByIdDto resGetCompanyByIdDto = ResGetCompanyByIdDto.builder()
                .companyEmail("test1@test.com")
                .companyPhoneNumber(12345678L)
                .brandName("testBrandName")
                .businessRegistrationNumber(12345678L)
                .managerId(UUID.randomUUID())
                .status(CompanyStatusEnum.PENDING)
                .build();

        List<ResGetCompanyByIdDto> responseDtos = new ArrayList<>();
        responseDtos.add(resGetCompanyByIdDto);
        return ResponseDto.of("업체목록이 조회되었습니다.", responseDtos);
    }
}
