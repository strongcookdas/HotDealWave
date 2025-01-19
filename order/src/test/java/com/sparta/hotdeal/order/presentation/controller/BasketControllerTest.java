package com.sparta.hotdeal.order.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hotdeal.order.application.dtos.basket.req.ReqPatchBasketDto;
import com.sparta.hotdeal.order.application.dtos.basket.req.ReqPostBasketDto;
import com.sparta.hotdeal.order.application.dtos.basket.res.ResDeleteBasketDto;
import com.sparta.hotdeal.order.application.dtos.basket.res.ResGetBasketByIdDto;
import com.sparta.hotdeal.order.application.dtos.basket.res.ResGetBasketListDto;
import com.sparta.hotdeal.order.application.dtos.basket.res.ResPatchBasketDto;
import com.sparta.hotdeal.order.application.dtos.basket.res.ResPostBasketDto;
import com.sparta.hotdeal.order.application.service.BasketService;
import com.sparta.hotdeal.order.infrastructure.custom.RequestUserDetails;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@WebMvcTest(BasketController.class)
class BasketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BasketService basketService;

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
    @DisplayName("장바구니 생성 API 성공 테스트")
    void createBasket() throws Exception {
        ReqPostBasketDto req = ReqPostBasketDto.builder()
                .productId(UUID.randomUUID())
                .quantity(2)
                .build();

        ResPostBasketDto res = ResPostBasketDto.builder()
                .basketId(UUID.randomUUID())
                .build();

        when(basketService.createBasket(any(), any())).thenReturn(res);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/baskets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("장바구니 단건 조회 API 성공 테스트")
    void getBasketDetail() throws Exception {
        UUID basketId = UUID.randomUUID();

        ResGetBasketByIdDto res = ResGetBasketByIdDto.builder()
                .basketId(basketId)
                .build();

        when(basketService.getBasketDetail(any(), any())).thenReturn(res);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/baskets/{basketId}", basketId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("장바구니 목록 조회 API 성공 테스트")
    void getBasketList() throws Exception {
        ResGetBasketListDto basket = ResGetBasketListDto.builder()
                .basketId(UUID.randomUUID())
                .build();

        Page<ResGetBasketListDto> page = new PageImpl<>(Collections.singletonList(basket), PageRequest.of(0, 10), 1);

        when(basketService.getBasketList(any(), any())).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/baskets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("장바구니 수량 수정 API 성공 테스트")
    void updateBasket() throws Exception {
        UUID basketId = UUID.randomUUID();

        ReqPatchBasketDto req = ReqPatchBasketDto.builder()
                .quantity(5)
                .build();

        ResPatchBasketDto res = ResPatchBasketDto.builder()
                .basketId(basketId)
                .build();

        when(basketService.updateBasket(any(), any(), any())).thenReturn(res);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/baskets/{basketId}", basketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("장바구니 삭제 API 성공 테스트")
    void deleteBasket() throws Exception {
        UUID basketId = UUID.randomUUID();

        ResDeleteBasketDto res = ResDeleteBasketDto.builder()
                .basketId(basketId)
                .build();

        when(basketService.deleteBasket(any(), any(), any())).thenReturn(res);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/baskets/{basketId}", basketId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}