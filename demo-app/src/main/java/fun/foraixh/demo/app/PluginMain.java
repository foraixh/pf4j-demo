package fun.foraixh.demo.app;

import fun.foraixh.definition.Greeting;
import java.nio.file.Path;
import java.util.List;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;

/**
 * @Author foraixh
 * @Date 2022-07-24 17:02:00
 * @Version 1.0
 * @Usage usage
 */
public class PluginMain {
    public static void main(String[] args) {
        PluginManager pluginManager = new DefaultPluginManager(
            Path.of("D:\\0-opensource-projects\\pf4j-demo\\demo-app\\src\\main\\resources\\plugins"));
        pluginManager.loadPlugins();
        pluginManager.startPlugins();

        List<Greeting> greetings = pluginManager.getExtensions(Greeting.class);
        for (Greeting greeting : greetings) {
            System.out.println(">>> " + greeting.greet("foraixh"));
        }
    }
}
