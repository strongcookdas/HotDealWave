package com.sparta.hotdeal.coupon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hotdeal.coupon.application.dto.req.ReqPostCouponValidateDto;
import com.sparta.hotdeal.coupon.application.dto.req.ReqPostCouponsIssueDto;
import com.sparta.hotdeal.coupon.application.dto.res.ResGetUserCouponsDto;
import com.sparta.hotdeal.coupon.application.dto.res.ResPostCouponValidateDto;
import com.sparta.hotdeal.coupon.application.service.CouponService;
import com.sparta.hotdeal.coupon.infrastructure.custom.RequestUserDetails;
import com.sparta.hotdeal.coupon.presentation.controller.CouponController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@WebMvcTest(CouponController.class)
public class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CouponService couponService;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();

        // Spring Security 컨텍스트에 사용자 정보 설정
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        new RequestUserDetails(
                                UUID.randomUUID().toString(), // userId
                                "test@example.com",                       // email
                                List.of(new SimpleGrantedAuthority("ROLE_MASTER")) // 권한
                        ),
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_MASTER"))
                )
        );
    }


    @Test
    @DisplayName("선착순 쿠폰 발급 API 성공 테스트")
    public void testIssueFirstComeFirstServeCoupon() throws Exception {
        UUID couponInfoId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        ReqPostCouponsIssueDto reqDto = new ReqPostCouponsIssueDto(couponInfoId);

        String requestBody = objectMapper.writeValueAsString(reqDto);

        Mockito.doNothing().when(couponService).issueFirstComeFirstServeCoupon(Mockito.eq(userId), Mockito.any());

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/coupons/issue")
                                .header("userId", userId.toString())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isCreated()
                );
    }

    @Test
    @DisplayName("사용자 쿠폰 목록 조회 API 성공 테스트")
    public void testGetUserCoupons() throws Exception {
        UUID userId = UUID.randomUUID();

        Mockito.when(couponService.getUserCoupons(Mockito.eq(userId), Mockito.eq(false)))
                .thenReturn(Arrays.asList(new ResGetUserCouponsDto()));

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/coupons")
                                .param("isUsed", "false")
                                .param("userId", userId.toString())
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk()
                );
    }

    @Test
    @DisplayName("사용자 쿠폰 상세 조회 API 성공 테스트")
    public void testGetUserCouponDetail() throws Exception {
        UUID couponId = UUID.randomUUID();

        Mockito.when(couponService.getUserCouponDetail(Mockito.any(UUID.class)))
                .thenReturn(new ResGetUserCouponsDto());

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/coupons/{couponId}", couponId)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk()
                );
    }

    @Test
    @DisplayName("쿠폰 유효성 검사 API 성공 테스트")
    public void testValidateCoupon() throws Exception {
        UUID couponId = UUID.randomUUID();
        ReqPostCouponValidateDto.Product product1 = new ReqPostCouponValidateDto.Product(
                UUID.randomUUID(),
                2,
                5000
        );

        ReqPostCouponValidateDto.Product product2 = new ReqPostCouponValidateDto.Product(
                null,
                1,
                15000
        );
        List<ReqPostCouponValidateDto.Product> products = Arrays.asList(product1, product2);
        ReqPostCouponValidateDto reqDto = new ReqPostCouponValidateDto(products);

        String requestBody = objectMapper.writeValueAsString(reqDto);

        Mockito.when(couponService.validateCoupon(Mockito.eq(couponId), Mockito.any()))
                .thenReturn(new ResPostCouponValidateDto(true, 1000));

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/coupons/{couponId}/validate", couponId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk()
                );
    }

    @Test
    @DisplayName("쿠폰 사용 API 성공 테스트")
    public void testUseCoupon() throws Exception {
        UUID couponId = UUID.randomUUID();

        Mockito.doNothing().when(couponService).useCoupon(Mockito.eq(couponId));

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/coupons/{couponId}/use", couponId)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk()
                );
    }

    @Test
    @DisplayName("쿠폰 회복 API 성공 테스트")
    public void testRecoverCoupon() throws Exception {
        UUID couponId = UUID.randomUUID();

        Mockito.doNothing().when(couponService).recoverCoupon(Mockito.eq(couponId));

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/coupons/{couponId}/recover", couponId)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk()
                );
    }

    @Test
    @DisplayName("사용자 쿠폰 삭제 API 성공 테스트")
    public void testDeleteCoupon() throws Exception {
        UUID couponId = UUID.randomUUID();
        String email = "test@example.com";

        Mockito.doNothing().when(couponService).deleteCoupon(Mockito.eq(couponId), Mockito.eq(email));

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/v1/coupons/{couponId}", couponId)
                                .header("email", email)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk()
                );
    }
}
