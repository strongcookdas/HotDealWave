package com.sparta.hotdeal.order.infrastructure.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 상품/주소 병렬 조회(CompletableFuture.supplyAsync)에 쓸 전용 Executor.
 * 별도 Executor를 지정하지 않으면 JVM 전체가 공유하는 ForkJoinPool.commonPool()
 * (크기: CPU 코어 수 - 1)로 위임되는데, 이 풀은 HTTP 요청 동시성과 무관하게 고정된
 * 크기라 동시 요청이 몰리면 여기서 큐잉이 발생한다(2026-08 부하테스트에서 실측 확인).
 * 이 작업은 Feign 호출 응답을 기다리는 I/O 바운드 작업이라, CPU 코어 수보다
 * 훨씬 큰 풀을 둬도 무방하다.
 */
@Configuration
public class OrderAsyncConfig {

    @Bean
    public Executor orderParallelFetchExecutor() {
        return Executors.newFixedThreadPool(300);
    }
}
