package org.example.firstservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/first-service")
public class FirstServiceController {
    @GetMapping("/test")
    public String test() {
        return "test first service";
    }

    @GetMapping("/message")
    public String message(@RequestHeader("f-request") String header) {
        log.info(header);
        return "hello world in first service";
    }
}
