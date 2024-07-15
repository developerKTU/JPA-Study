package example.datajpa.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @GetMapping("/hello")
    public String hello(){
        String printString = "Hello";
        log.info(printString);
        return printString;
    }

}
