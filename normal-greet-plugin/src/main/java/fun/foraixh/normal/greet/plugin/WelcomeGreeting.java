package fun.foraixh.normal.greet.plugin;

import fun.foraixh.definition.Greeting;
import lombok.NoArgsConstructor;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
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
@NoArgsConstructor
public class WelcomeGreeting implements Greeting {
    private DemoHandler demoHandler;

    public WelcomeGreeting(DemoHandler demoHandler) {
        this.demoHandler = demoHandler;
    }

    @GetMapping("/demo/test11")
    public String test10(String str) {
        return demoHandler.demo(str);
    }

    @Override
    public String greet(String name) {
        return "welcome " + name;
    }
}
