package com.sparta.hotdeal.product.application.service.file;

import com.sparta.hotdeal.product.application.exception.ApplicationException;
import com.sparta.hotdeal.product.application.exception.ErrorCode;
import com.sparta.hotdeal.product.domain.entity.product.File;
import com.sparta.hotdeal.product.domain.entity.product.ImageTypeEnum;
import com.sparta.hotdeal.product.domain.entity.product.SubFile;
import com.sparta.hotdeal.product.domain.repository.product.SubFileRepository;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class SubFileService {

    @Value("${s3.domain}")
    private String domain;

    private final FileUploadService fileUploadService;
    private final SubFileRepository subFileRepository;

    public SubFile saveImg(MultipartFile img, File file, ImageTypeEnum imageType) {
        String path = imageType.getValue();
        String filename = UUID.randomUUID().toString().replace("-", "") + "_"
                + img.getOriginalFilename();
        String fileUrl = domain + "/" + path + "/" + filename;

        SubFile subFile = SubFile.of(fileUrl, file);
        subFileRepository.save(subFile);

        try {
            fileUploadService.uploadFile(img, path, filename);
        } catch (IOException e) {
            throw new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
        }

        return subFile;
    }

    public SubFile updateImg(MultipartFile file, File detailImgsFile, String username, ImageTypeEnum imageType) {
        // 기존 SubFile 삭제 (S3에서 파일 삭제)
        detailImgsFile.getSubFiles().forEach(subFile -> {
            String resource = subFile.getResource();
            String fileKey = getFileKeyFromUrl(resource);

            // S3 SubFile 삭제
            fileUploadService.deleteFile(fileKey);

            // SubFile 삭제
            subFile.delete(username);
        });

        // 새로운 SubFile 추가
        SubFile newFile = saveImg(file, detailImgsFile, imageType);
        return newFile;
    }

    public void updateSubFiles(List<MultipartFile> newFiles, File existingFile, String username,
                               ImageTypeEnum imageType) {
        // 기존 SubFile들 확인하고 삭제해야 할 SubFile이 있으면 삭제
        List<SubFile> existingSubFiles = existingFile.getSubFiles();

        // 기존 SubFile 중에 빠진 파일들 삭제
        for (SubFile subFile : existingSubFiles) {
            boolean isExist = newFiles.stream()
                    .anyMatch(newFile -> subFile.getResource().equals(newFile.getOriginalFilename()));

            if (!isExist) {
                String resource = subFile.getResource();
                String fileKey = getFileKeyFromUrl(resource);

                // S3 SubFile 삭제
                fileUploadService.deleteFile(fileKey);

                // SubFile 삭제
                subFile.delete(username);
            }
        }

        // 새로운 SubFile 추가
        for (MultipartFile newFile : newFiles) {
            boolean isExist = existingSubFiles.stream()
                    .anyMatch(subFile -> subFile.getResource().equals(newFile.getOriginalFilename()));
            if (!isExist) {
                saveImg(newFile, existingFile, imageType); // 새로운 파일 추가
            }
        }
    }

    public void deleteImg(File detailImgsFile, String username) {

        List<SubFile> existingSubFiles = detailImgsFile.getSubFiles();
        for (SubFile subFile : existingSubFiles) {
            String resource = subFile.getResource();
            String fileKey = getFileKeyFromUrl(resource);

            // S3 SubFile 삭제
            fileUploadService.deleteFile(fileKey);

            if (subFile.getDeletedAt() == null) {
                subFile.delete(username);
            }
        }
    }

    public String getFileKeyFromUrl(String url) {
        try {
            URI uri = new URI(url);
            return uri.getPath().substring(1);
        } catch (URISyntaxException e) {
            throw new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
        }
    }
}
