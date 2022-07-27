package fun.foraixh.normal.greet.plugin;

import fun.foraixh.definition.Greeting;
import fun.foraixh.demo.app.handler.AppHandler;
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
@NoArgsConstructor
@RestController
public class WelcomeGreeting implements Greeting {
    @Autowired
    private DemoHandler demoHandler;

    @Autowired
    private AppHandler appHandle;

    // public WelcomeGreeting() {
    //
    // }

    // public WelcomeGreeting(DemoHandler demoHandler) {
    //     this.demoHandler = demoHandler;
    // }

    @GetMapping("/demo/appHandle")
    public String appHandle(String str) {
        return appHandle.demo(str);
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
