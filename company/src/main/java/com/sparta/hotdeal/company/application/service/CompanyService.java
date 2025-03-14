package com.sparta.hotdeal.company.application.service;

import com.sparta.hotdeal.company.application.dtos.company.ReqPatchCompanyByIdDto;
import com.sparta.hotdeal.company.application.dtos.company.ReqPatchCompanyByIdStatusDto;
import com.sparta.hotdeal.company.application.dtos.company.ReqPostCompanyDto;
import com.sparta.hotdeal.company.application.dtos.company.ResGetCompanyByIdDto;
import com.sparta.hotdeal.company.domain.entity.company.Company;
import com.sparta.hotdeal.company.domain.entity.company.CompanyStatusEnum;
import com.sparta.hotdeal.company.domain.repository.CompanyRepository;
import com.sparta.hotdeal.company.application.exception.ApplicationException;
import com.sparta.hotdeal.company.application.exception.ErrorCode;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;

    public void createCompany(ReqPostCompanyDto reqPostCompanyDto) {
        // (1) 업체 등록 번호 중복 확인
        companyRepository.findByBusinessRegistrationNumber(
                        reqPostCompanyDto.getBusinessRegistrationNumber())
                .ifPresent(company -> {
                    throw new ApplicationException(ErrorCode.ALREADY_EXIST_EXCEPTION);
                });

        Company company = Company.create(
                reqPostCompanyDto.getBusinessRegistrationNumber(),
                reqPostCompanyDto.getCompanyPhoneNumber(),
                reqPostCompanyDto.getManagerId(),
                reqPostCompanyDto.getCompanyEmail(),
                reqPostCompanyDto.getBrandName(),
                reqPostCompanyDto.getStatus()
        );
        companyRepository.save(company);
    }

    @Transactional
    public void updateCompany(UUID companyId, ReqPatchCompanyByIdDto reqPatchCompanyByIdDto) {
        // (1) 업체 유무 확인
        Company fetchedCompany = companyRepository.findById(companyId).orElseThrow(() -> new ApplicationException(
                ErrorCode.NOT_FOUND_EXCEPTION
        ));

        fetchedCompany.update(reqPatchCompanyByIdDto.getCompanyPhoneNumber(), reqPatchCompanyByIdDto.getManagerId(),
                reqPatchCompanyByIdDto.getCompanyEmail(), reqPatchCompanyByIdDto.getBrandName());
    }

    @Transactional
    public void updateCompanyStatus(UUID companyId, ReqPatchCompanyByIdStatusDto reqPatchCompanyByIdStatusDto) {
        // (1) 업체 유무 확인
        Company fetchedCompany = companyRepository.findById(companyId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
        // (2) 상태 값 유효성 검사
        if (!CompanyStatusEnum.isValidStatus(String.valueOf(reqPatchCompanyByIdStatusDto.getStatus()))) {
            throw new ApplicationException(ErrorCode.INVALID_VALUE_EXCEPTION);
        }

        fetchedCompany.updateStatus(reqPatchCompanyByIdStatusDto.getStatus());
    }

    public ResGetCompanyByIdDto getCompanyById(UUID companyId) {
        // (1) 업체 유무 확인
        Company fetchedCompany = companyRepository.findById(companyId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        return ResGetCompanyByIdDto.builder()
                .companyEmail(fetchedCompany.getCompanyEmail())
                .companyPhoneNumber(fetchedCompany.getCompanyPhoneNumber())
                .brandName(fetchedCompany.getBrandName())
                .businessRegistrationNumber(fetchedCompany.getBusinessRegistrationNumber())
                .managerId(fetchedCompany.getManagerId())
                .status(fetchedCompany.getStatus())
                .build();
    }

    public Page<ResGetCompanyByIdDto> getCompanyList(Pageable pageable, CompanyStatusEnum status) {
        if (status != null) {
            return companyRepository.findByStatus(status, pageable)
                    .map(ResGetCompanyByIdDto::create);
        } else {
            return companyRepository.findAll(pageable)
                    .map(ResGetCompanyByIdDto::create);
        }
    }
}
