package com.sparta.hotdeal.product.application.service;

import com.sparta.hotdeal.product.domain.entity.product.File;
import com.sparta.hotdeal.product.domain.entity.product.SubFile;
import com.sparta.hotdeal.product.domain.repository.product.SubFileRepository;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class SubFileService {

    private final SubFileRepository subFileRepository;

    @Transactional
    public SubFile saveImg(MultipartFile img, File file) {
        String filename = UUID.randomUUID().toString().replace("-", "") + "_" + img.getOriginalFilename();

        SubFile subFile = SubFile.of(filename, file);
        subFileRepository.save(subFile);

        // TODO: s3 연결하는 작업 필요

        return subFile;
    }
}
