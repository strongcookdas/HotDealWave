package com.sparta.hotdeal.product.application.service;

import com.sparta.hotdeal.product.domain.entity.product.File;
import com.sparta.hotdeal.product.domain.repository.product.FileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class FileService {

    private final FileRepository fileRepository;

    @Transactional
    public File saveFile() {
        File file = fileRepository.save(new File());
        return file;
    }
}
