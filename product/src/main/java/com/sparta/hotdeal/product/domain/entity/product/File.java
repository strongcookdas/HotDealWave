package com.sparta.hotdeal.product.domain.entity.product;

import com.sparta.hotdeal.product.domain.entity.AuditingDate;
import com.sparta.hotdeal.product.domain.entity.review.Review;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "p_file")
@Where(clause = "deleted_at IS NULL")
public class File extends AuditingDate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // SubFile과 1:N 관계 설정
    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubFile> subFiles;

    // Product와 detailImgs의 1:1 관계
    @OneToOne(mappedBy = "detailImgs")
    private Product productDetailImgs;

    // Product와 thumbImg의 1:1 관계
    @OneToOne(mappedBy = "thumbImg")
    private Product productThumbImg;

    // Review와 reviewImgs의 1:1 관계
    @OneToOne(mappedBy = "reviewImgs")
    private Review reviewImgs;
}
