package com.sparta.hotdeal.product.application.service.file;

import com.sparta.hotdeal.product.domain.entity.product.File;
import com.sparta.hotdeal.product.domain.entity.product.SubFile;
import com.sparta.hotdeal.product.domain.repository.product.SubFileRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class SubFileService {

    private final SubFileRepository subFileRepository;

    public SubFile saveImg(MultipartFile img, File file) {
        String filename = UUID.randomUUID().toString().replace("-", "") + "_" + img.getOriginalFilename();

        SubFile subFile = SubFile.of(filename, file);
        subFileRepository.save(subFile);

        // TODO: s3 연결하는 작업 필요

        return subFile;
    }

    public SubFile updateImg(MultipartFile file, File detailImgsFile, String username) {
        // 기존 SubFile 삭제 (S3에서 파일 삭제)
        detailImgsFile.getSubFiles().forEach(subFile -> {
            // TODO: S3에서 파일 삭제

            // SubFile 삭제
            subFile.delete(username);
        });

        // 새로운 SubFile 추가
        SubFile newFile = saveImg(file, detailImgsFile);

        return newFile;
    }

    public void updateSubFiles(List<MultipartFile> newFiles, File existingFile, String username) {
        // 기존 SubFile들 확인하고 삭제해야 할 SubFile이 있으면 삭제
        List<SubFile> existingSubFiles = existingFile.getSubFiles();

        // 기존 SubFile 중에 빠진 파일들 삭제
        for (SubFile subFile : existingSubFiles) {
            boolean isExist = newFiles.stream()
                    .anyMatch(newFile -> subFile.getResource().equals(newFile.getOriginalFilename()));
            if (!isExist) {
                // TODO: S3에서 파일 삭제

                // SubFile 삭제
                subFile.delete(username);
            }
        }

        // 새로운 SubFile 추가
        for (MultipartFile newFile : newFiles) {
            boolean isExist = existingSubFiles.stream()
                    .anyMatch(subFile -> subFile.getResource().equals(newFile.getOriginalFilename()));
            if (!isExist) {
                saveImg(newFile, existingFile); // 새로운 파일 추가
            }
        }
    }

    public void deleteImg(File detailImgsFile, String username) {

        List<SubFile> existingSubFiles = detailImgsFile.getSubFiles();
        for (SubFile subFile : existingSubFiles) {
            // TODO: s3에서 실제 파일 삭제

            if (subFile.getDeletedAt() == null) {
                subFile.delete(username);
            }
        }
    }
}
