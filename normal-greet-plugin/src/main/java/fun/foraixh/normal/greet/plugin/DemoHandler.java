package fun.foraixh.normal.greet.plugin;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @Author foraixh
 * @Date 2022-07-25 16:47:00
 * @Version 1.0
 * @Usage usage
 */
@Component
public class DemoHandler {
    public String demo(String str) {
        return this.getClass().getSimpleName() + "-" + str;
    }
}
