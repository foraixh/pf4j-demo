package fun.foraixh.demo.app.runner;

import fun.foraixh.definition.Greeting;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.PluginManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @Author foraixh
 * @Date 2022-07-25 07:22:00
 * @Version 1.0
 * @Usage usage
 */
@Component
@Slf4j
public class DemoRunner implements CommandLineRunner {
    private final PluginManager pluginManager;

    public DemoRunner(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Greeting> greetings = pluginManager.getExtensions(Greeting.class);

        log.info("found extensions size: {}", greetings.size());

        for (Greeting greeting : greetings) {
            System.out.println(">>> " + greeting.greet("foraixh"));
        }
    }
}
