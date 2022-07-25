package fun.foraixh.normal.greet.plugin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author foraixh
 * @Date 2022-07-25 15:49:00
 * @Version 1.0
 * @Usage usage
 */
@RestController
public class DemoController {
    @GetMapping("/demo/test10")
    public String test10(String str) {
        return str;
    }
}
