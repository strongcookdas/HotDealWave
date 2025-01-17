package com.sparta.hotdeal.coupon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hotdeal.coupon.application.dto.req.ReqPatchCouponInfosByIdStatusDto;
import com.sparta.hotdeal.coupon.application.dto.req.ReqPostCouponInfosDto;
import com.sparta.hotdeal.coupon.application.dto.req.ReqPutCouponInfosByIdDto;
import com.sparta.hotdeal.coupon.application.dto.res.ResGetCouponInfosByIdDto;
import com.sparta.hotdeal.coupon.application.dto.res.ResPostCouponInfosDto;
import com.sparta.hotdeal.coupon.application.service.CouponInfoService;
import com.sparta.hotdeal.coupon.domain.entity.CouponStatus;
import com.sparta.hotdeal.coupon.infrastructure.custom.RequestUserDetails;
import com.sparta.hotdeal.coupon.presentation.controller.CouponInfoController;
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

import java.util.List;
import java.util.UUID;

@WebMvcTest(CouponInfoController.class)
public class CouponInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CouponInfoService couponInfoService;

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
                                "test@example.com",           // email
                                List.of(new SimpleGrantedAuthority("ROLE_MASTER")) // 권한
                        ),
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_MASTER"))
                )
        );
    }

    @Test
    @DisplayName("쿠폰 정보 생성 API 성공 테스트")
    public void testCreateCoupon() throws Exception {
        ReqPostCouponInfosDto reqDto = new ReqPostCouponInfosDto();
        String requestBody = objectMapper.writeValueAsString(reqDto);

        ResPostCouponInfosDto responseDto = new ResPostCouponInfosDto(UUID.randomUUID());

        Mockito.when(couponInfoService.createCoupon(Mockito.any()))
                .thenReturn(responseDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/coupon-infos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isCreated()
                );
    }

    @Test
    @DisplayName("쿠폰 정보 수정 API 성공 테스트")
    public void testUpdateCoupon() throws Exception {
        UUID couponInfoId = UUID.randomUUID();
        ReqPutCouponInfosByIdDto reqDto = new ReqPutCouponInfosByIdDto();
        String requestBody = objectMapper.writeValueAsString(reqDto);

        Mockito.doNothing().when(couponInfoService).updateCoupon(Mockito.eq(couponInfoId), Mockito.any());

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/api/v1/coupon-infos/{couponInfoId}", couponInfoId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk()
                );
    }

    @Test
    @DisplayName("쿠폰 상태 변경 API 성공 테스트")
    public void testUpdateCouponStatus() throws Exception {
        UUID couponInfoId = UUID.randomUUID();
        ReqPatchCouponInfosByIdStatusDto reqDto = new ReqPatchCouponInfosByIdStatusDto(CouponStatus.ISSUED);
        String requestBody = objectMapper.writeValueAsString(reqDto);

        Mockito.doNothing().when(couponInfoService).updateCouponStatus(Mockito.eq(couponInfoId), Mockito.any());

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/api/v1/coupon-infos/{couponInfoId}/status", couponInfoId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk()
                );
    }

    @Test
    @DisplayName("쿠폰 상세 조회 API 성공 테스트")
    public void testGetCouponDetail() throws Exception {
        UUID couponInfoId = UUID.randomUUID();
        ResGetCouponInfosByIdDto responseDto = new ResGetCouponInfosByIdDto();

        Mockito.when(couponInfoService.getCouponDetail(Mockito.eq(couponInfoId)))
                .thenReturn(responseDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/coupon-infos/{couponInfoId}", couponInfoId)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk()
                );
    }
}
