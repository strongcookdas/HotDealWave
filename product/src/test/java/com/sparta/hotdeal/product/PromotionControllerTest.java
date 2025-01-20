package com.sparta.hotdeal.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hotdeal.product.application.dtos.req.promotion.ReqPostPromotionDto;
import com.sparta.hotdeal.product.application.dtos.req.promotion.ReqPutPromotionDto;
import com.sparta.hotdeal.product.application.dtos.res.promotion.ResGetPromotionDto;
import com.sparta.hotdeal.product.application.dtos.res.promotion.ResPostPromotionDto;
import com.sparta.hotdeal.product.application.dtos.res.promotion.ResPutPromotionDto;
import com.sparta.hotdeal.product.application.service.promotion.PromotionService;
import com.sparta.hotdeal.product.domain.entity.promotion.PromotionStatusEnum;
import com.sparta.hotdeal.product.infrastructure.custom.RequestUserDetails;
import com.sparta.hotdeal.product.presentation.controller.PromotionController;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
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

@WebMvcTest(PromotionController.class)
public class PromotionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PromotionService promotionService;

    @InjectMocks
    private PromotionController promotionController;

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
    @DisplayName("프로모션 등록 API 성공 테스트")
    public void testAddPromotion() throws Exception {
        // ReqPostPromotionDto 객체 생성
        ReqPostPromotionDto reqDto = new ReqPostPromotionDto(
                UUID.randomUUID(), // productId
                LocalDateTime.now(), // start
                LocalDateTime.now().plusDays(7), // end
                5000, // discountPrice
                10 // quantity
        );

        ResPostPromotionDto resDto = Mockito.mock(ResPostPromotionDto.class);

        Mockito.when(promotionService.createPromotion(Mockito.any(ReqPostPromotionDto.class)))
                .thenReturn(resDto);

        String requestBody = objectMapper.writeValueAsString(reqDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/promotions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("프로모션 수정 API 성공 테스트")
    public void testUpdatePromotion() throws Exception {
        UUID promotionId = UUID.randomUUID();

        // ReqPutPromotionDto 객체 생성
        ReqPutPromotionDto reqDto = new ReqPutPromotionDto(
                UUID.randomUUID(), // productId
                LocalDateTime.now(), // start
                LocalDateTime.now().plusDays(7), // end
                5000, // discountPrice
                10 // quantity
        );

        ResPutPromotionDto resDto = Mockito.mock(ResPutPromotionDto.class);

        // Mockito stubbing 수정
        Mockito.when(promotionService.updatePromotion(Mockito.eq(promotionId), Mockito.any(ReqPutPromotionDto.class)))
                .thenReturn(resDto);

        String requestBody = objectMapper.writeValueAsString(reqDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/api/v1/promotions/{promotionId}", promotionId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("프로모션 삭제 API 성공 테스트")
    public void testDeletePromotion() throws Exception {
        UUID promotionId = UUID.randomUUID();

        Mockito.doNothing().when(promotionService).deletePromotion(Mockito.eq(promotionId), Mockito.anyString());

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/v1/promotions/{promotionId}", promotionId)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk()
                );
    }

    @Test
    @DisplayName("특정 프로모션 조회 API 성공 테스트")
    public void testGetPromotionDetail() throws Exception {
        UUID promotionId = UUID.randomUUID();
        ResGetPromotionDto resDto = Mockito.mock(ResGetPromotionDto.class);

        Mockito.when(promotionService.getPromotion(Mockito.eq(promotionId)))
                .thenReturn(resDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/promotions/{promotionId}", promotionId)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk()
                );
    }

    @Test
    @DisplayName("프로모션 목록 조회 API 성공 테스트")
    public void testGetAllPromotions() throws Exception {
        // Mock 데이터 준비
        Page<ResGetPromotionDto> promotionPage = Mockito.mock(Page.class);

        // Mockito stubbing 수정
        Mockito.when(promotionService.getAllPromotions(
                        Mockito.anyInt(), // pageNumber
                        Mockito.anyInt(), // pageSize
                        Mockito.anyString(), // sortBy
                        Mockito.anyString(), // direction
                        Mockito.anyList(), // productIds
                        Mockito.any(PromotionStatusEnum.class) // status
                ))
                .thenReturn(promotionPage);

        // MockMvc 요청
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/promotions")
                                .param("page_number", "1")
                                .param("page_size", "10")
                                .param("sort_by", "createdAt")
                                .param("direction", "asc")
                                .param("product_ids", "product1,product2") // 예시로 ID 리스트 추가
                                .param("status", "PENDING")
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk()
                );
    }
}
