package com.thangnt.ddd.controller.resource;

import com.thangnt.ddd.application.service.EventAppService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/resiliencecheck")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ResilienceTestController {

    private EventAppService eventAppService;

    RestTemplate restTemplate;

    @GetMapping("/rate-limit")
    @RateLimiter(name = "backendA", fallbackMethod = "rateLimitFallBack")
    public String rateLimitCheck(){
        return eventAppService.showEvent();
    }

    public String rateLimitFallBack(Throwable throwable){
        return "too many request !!!";
    }

    @GetMapping("/circuit-breaker")
    @CircuitBreaker(name = "cbBackEndCheck", fallbackMethod = "circuitBreakerFallBack")
    public String circuitBreakerCheck(){
        String url = "https://api.escuelajs.co/api/v1/categories/1";
        return restTemplate.getForObject(url, String.class);
    }

    public String circuitBreakerFallBack(Throwable throwable){
        return "Service Downed !!!";
    }
}
