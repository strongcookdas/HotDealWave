package com.sparta.hotdeal.product.domain.repository.product;

import com.sparta.hotdeal.product.domain.entity.product.SubFile;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubFileRepository extends JpaRepository<SubFile, UUID> {

    List<SubFile> findAllByFileIdIn(List<UUID> fileIds);
}
