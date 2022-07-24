package fun.foraixh.normal.greet.plugin;

import fun.foraixh.definition.Greeting;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

/**
 * @Author foraixh
 * @Date 2022-07-24 16:47:00
 * @Version 1.0
 * @Usage usage
 */
public class NormalGreetPlugin extends Plugin {
    public NormalGreetPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class WelcomeGreeting implements Greeting {

        @Override
        public String greet(String name) {
            return "welcome " + name;
        }
    }

    @Extension
    public static class WelcomeTwiceGreeting implements Greeting {

        @Override
        public String greet(String name) {
            return "welcome " + name + ", you are welcome.";
        }
    }
}
