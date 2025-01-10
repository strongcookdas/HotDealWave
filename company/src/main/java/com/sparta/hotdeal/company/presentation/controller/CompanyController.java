package com.sparta.hotdeal.company.presentation.controller;

import com.sparta.hotdeal.company.application.service.CompanyService;
import com.sparta.hotdeal.company.domain.entity.company.CompanyStatusEnum;
import com.sparta.hotdeal.company.application.dtos.company.ReqPatchCompanyByIdDto;
import com.sparta.hotdeal.company.application.dtos.company.ReqPatchCompanyByIdStatusDto;
import com.sparta.hotdeal.company.application.dtos.company.ReqPostCompanyDto;
import com.sparta.hotdeal.company.application.dtos.company.ResGetCompanyByIdDto;
import com.sparta.hotdeal.company.application.dtos.ResponseDto;
import com.sparta.hotdeal.company.infrastructure.security.RequestUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@Tag(name = "Company API", description = "업체 관련 API")
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Secured({"ROLE_SELLER", "ROLE_MASTER", "ROLE_MANAGER"})
    @Operation(summary = "업체 생성 API", description = "업체를 생성합니다.")
    public ResponseDto<Void> createCompany(@AuthenticationPrincipal RequestUserDetails userDetails,
                                           @Valid @RequestBody ReqPostCompanyDto reqPostCompanyDto) {
        companyService.createCompany(reqPostCompanyDto);
        return ResponseDto.of("업체가 신청되었습니다.", null);
    }

    @PatchMapping("/{companyId}")
    @Secured({"ROLE_SELLER", "ROLE_MASTER", "ROLE_MANAGER"})
    @Operation(summary = "업체 수정 API", description = "업체를 수정합니다.")
    public ResponseDto<Void> updateCompany(@AuthenticationPrincipal RequestUserDetails userDetails,
                                           @Valid @PathVariable UUID companyId,
                                           @RequestBody ReqPatchCompanyByIdDto reqPatchCompanyByIdDto) {
        companyService.updateCompany(companyId, reqPatchCompanyByIdDto);
        return ResponseDto.of("업체가 수정되었습니다.", null);
    }

    @PatchMapping("/{companyId}/status")
    @Secured({"ROLE_MASTER", "ROLE_MANAGER"})
    @Operation(summary = "업체 상태 변경 API", description = "업체 상태를 변경합니다.")
    public ResponseDto<Void> updateCompanyStatus(@AuthenticationPrincipal RequestUserDetails userDetails,
                                                 @Valid @PathVariable UUID companyId,
                                                 @RequestBody ReqPatchCompanyByIdStatusDto reqPatchCompanyByIdStatusDto) {
        companyService.updateCompanyStatus(companyId, reqPatchCompanyByIdStatusDto);
        return ResponseDto.of("상태가 수정되었습니다.", null);
    }

    @GetMapping("/{companyId}")
    @Secured({"ROLE_SELLER", "ROLE_MASTER", "ROLE_MANAGER"})
    @Operation(summary = "업체 상세 조회 API", description = "업체 ID로 상세 정보를 조회합니다.")
    public ResponseDto<ResGetCompanyByIdDto> getCompanyById(@AuthenticationPrincipal RequestUserDetails userDetails,
                                                            @PathVariable UUID companyId) {
        ResGetCompanyByIdDto resGetCompanyByIdDto = companyService.getCompanyById(companyId);
        return ResponseDto.of("업체가 조회되었습니다.", resGetCompanyByIdDto);
    }

    @GetMapping
    @Secured({"ROLE_SELLER", "ROLE_MASTER", "ROLE_MANAGER"})
    @Operation(summary = "업체 목록 조회 API", description = "업체 목록을 조회합니다.")
    public ResponseDto<Page<ResGetCompanyByIdDto>> getCompanyList(
            @AuthenticationPrincipal RequestUserDetails userDetails, Pageable pageable) {
        return ResponseDto.of("업체목록이 조회되었습니다.", companyService.getCompanyList(pageable));
    }
}
