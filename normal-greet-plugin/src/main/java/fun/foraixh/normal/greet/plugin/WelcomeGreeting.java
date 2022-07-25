package fun.foraixh.normal.greet.plugin;

import fun.foraixh.definition.Greeting;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author foraixh
 * @Date 2022-07-25 07:46:00
 * @Version 1.0
 * @Usage usage
 */
@Extension
@RestController
public class WelcomeGreeting implements Greeting {
    @GetMapping("/demo/test11")
    public String test10(String str) {
        return str;
    }

    @Override
    public String greet(String name) {
        return "welcome " + name;
    }
}
