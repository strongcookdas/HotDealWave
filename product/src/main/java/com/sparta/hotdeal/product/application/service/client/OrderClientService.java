package com.sparta.hotdeal.product.application.service.client;

import com.sparta.hotdeal.product.infrastructure.dtos.ResGetOrderByIdDto;
import java.util.UUID;

public interface OrderClientService {

    ResGetOrderByIdDto getOrder(UUID orderId);
}