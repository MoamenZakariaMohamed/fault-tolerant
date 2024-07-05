package com.fault.tolerance;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
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
}
