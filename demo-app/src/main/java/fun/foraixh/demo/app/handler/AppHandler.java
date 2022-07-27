package fun.foraixh.demo.app.handler;

import org.springframework.stereotype.Component;

/**
 * @Author foraixh
 * @Date 2022-07-27 17:14:00
 * @Version 1.0
 * @Usage usage
 */
@Component
public class AppHandler {
    public String demo(String str) {
        return this.getClass().getSimpleName() + "-" + str;
    }
}
