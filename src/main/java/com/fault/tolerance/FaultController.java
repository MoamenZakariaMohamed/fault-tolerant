package com.fault.tolerance;


import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequiredArgsConstructor
@Slf4j
public class FaultController {
    private final FaultService service;

    @GetMapping("/")
    public void send(){
        log.info("Inside Controller");
        service.send("This is the body.");
    }


    @GetMapping("/getMessage")
    public ResponseEntity<String> getMessage(@RequestParam(value="name", defaultValue = "Hello") String name){
        return service.rateTest(name);
    }

}
