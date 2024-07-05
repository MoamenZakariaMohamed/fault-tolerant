package com.fault.tolerance;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
public class FaultService {
    @CircuitBreaker(name = "cb")
    @Retry(name = "demo-retry", fallbackMethod = "fallback")
    public void send(String msg) {
        log.info("Sending Data");
        RestClient client = RestClient.builder().build();
        client.post().uri("http://localhost:12000/")
                .contentType(MediaType.TEXT_PLAIN)
                .body(msg)
                .retrieve()
                .onStatus(HttpStatusCode::is2xxSuccessful, (req, res) -> System.out.println("Success")).
                onStatus(HttpStatusCode::isError, (req, res) -> System.out.println("Error")).toBodilessEntity();



    }


    @RateLimiter(name = "getMessageRateLimit", fallbackMethod = "getMessageFallBack")
    public  ResponseEntity<String> rateTest(String name) {
        return ResponseEntity.ok().body("Message from getMessage() :" +name);
    }
    private ResponseEntity<String> getMessageFallBack(RequestNotPermitted exception) {

        log.info("Rate limit has applied, So no further calls are getting accepted");

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Too many requests : No further request will be accepted. Please try after sometime");
    }


    public void fallback(String msg, Exception ex){
        log.info("Inside Fallback for retry");
    }


}
