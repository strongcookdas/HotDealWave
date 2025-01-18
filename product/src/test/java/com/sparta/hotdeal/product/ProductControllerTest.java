package com.sparta.hotdeal.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hotdeal.product.application.dtos.req.product.ReqPostProductDto;
import com.sparta.hotdeal.product.application.dtos.req.product.ReqPutProductDto;
import com.sparta.hotdeal.product.application.dtos.res.product.ResGetProductDto;
import com.sparta.hotdeal.product.application.dtos.res.product.ResPostProductDto;
import com.sparta.hotdeal.product.application.dtos.res.product.ResPutProductDto;
import com.sparta.hotdeal.product.application.service.product.ProductInventoryService;
import com.sparta.hotdeal.product.application.service.product.ProductService;
import com.sparta.hotdeal.product.infrastructure.custom.RequestUserDetails;
import com.sparta.hotdeal.product.presentation.controller.ProductController;
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
import org.springframework.mock.web.MockMultipartFile;
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

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private ProductInventoryService productInventoryService;

    @InjectMocks
    private ProductController productController;

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
    @DisplayName("상품 등록 API 성공 테스트")
    public void testAddProduct() throws Exception {
        UUID companyId = UUID.randomUUID();
        MockMultipartFile thumbImg = new MockMultipartFile("thumbImg", "thumb.jpg", "image/jpeg",
                "image content".getBytes());
        MockMultipartFile detailImg = new MockMultipartFile("detailImgs", "detail.jpg", "image/jpeg",
                "image content".getBytes());

        ResPostProductDto resDto = Mockito.mock(ResPostProductDto.class);

        Mockito.when(productService.createProduct(Mockito.any(ReqPostProductDto.class)))
                .thenReturn(resDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.multipart("/api/v1/products")
                                .file(thumbImg)
                                .file(detailImg)
                                .param("name", "Product Name")
                                .param("price", "1000")
                                .param("stock", "100")
                                .param("category", "OFFICE_SUPPLIES")
                                .param("companyId", companyId.toString())
                                .param("description", "Description")
                )
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("상품 수정 API 성공 테스트")
    public void testUpdateProduct() throws Exception {
        UUID productId = UUID.randomUUID();
        UUID companyId = UUID.randomUUID();
        MockMultipartFile thumbImg = new MockMultipartFile("thumbImg", "thumb.jpg", "image/jpeg",
                "image content".getBytes());
        MockMultipartFile detailImg = new MockMultipartFile("detailImgs", "detail.jpg", "image/jpeg",
                "image content".getBytes());

        ResPutProductDto resDto = Mockito.mock(ResPutProductDto.class);

        Mockito.when(productService.updateProduct(Mockito.eq(productId), Mockito.any(ReqPutProductDto.class),
                        Mockito.anyString()))
                .thenReturn(resDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.multipart("/api/v1/products/{productId}", productId)
                                .file(thumbImg)
                                .file(detailImg)
                                .param("name", "Updated Product Name")
                                .param("price", "2000")
                                .param("stock", "200")
                                .param("category", "OFFICE_SUPPLIES")
                                .param("companyId", companyId.toString())
                                .param("description", "Updated Description")
                                .with(request -> {
                                    request.setMethod("PUT");
                                    return request;
                                })
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("상품 삭제 API 성공 테스트")
    public void testDeleteProduct() throws Exception {
        UUID productId = UUID.randomUUID();

        Mockito.doNothing().when(productService).deleteProduct(Mockito.eq(productId), Mockito.anyString());

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/v1/products/{productId}", productId)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk()
                );
    }

    @Test
    @DisplayName("상품 상세 조회 API 성공 테스트")
    public void testGetProductDetail() throws Exception {
        UUID productId = UUID.randomUUID();
        ResGetProductDto resDto = Mockito.mock(ResGetProductDto.class);

        Mockito.when(productService.getProduct(Mockito.eq(productId)))
                .thenReturn(resDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/products/{productId}", productId)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk()
                );
    }

    @Test
    @DisplayName("상품 목록 조회 API 성공 테스트")
    public void testGetAllProducts() throws Exception {
        Page<ResGetProductDto> productPage = Mockito.mock(Page.class);

        Mockito.when(productService.getAllProducts(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(),
                        Mockito.anyString(), Mockito.anyString(), Mockito.anyList()))
                .thenReturn(productPage);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/products")
                                .param("page_number", "1")
                                .param("page_size", "10")
                                .param("sort_by", "createdAt")
                                .param("direction", "asc")
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk()
                );
    }
}
