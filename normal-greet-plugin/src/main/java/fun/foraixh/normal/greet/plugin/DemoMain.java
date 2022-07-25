package fun.foraixh.normal.greet.plugin;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Author foraixh
 * @Date 2022-07-25 19:43:00
 * @Version 1.0
 * @Usage usage
 */
@Slf4j
public class DemoMain {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext =
            new AnnotationConfigApplicationContext("fun.foraixh.normal.greet.plugin");
        log.info("ApplicationContext created, has beans = " + Arrays.toString(applicationContext.getBeanDefinitionNames()));

    }
}
