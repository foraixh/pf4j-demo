package fun.foraixh.normal.greet.plugin;

import fun.foraixh.definition.Greeting;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;

/**
 * @Author foraixh
 * @Date 2022-07-25 07:46:00
 * @Version 1.0
 * @Usage usage
 */
@Extension
public class WelcomeTwiceGreeting implements Greeting {

    @Override
    public String greet(String name) {
        return "welcome " + name + ", you are welcome.";
    }
}
