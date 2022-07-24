package fun.foraixh.definition;

import org.pf4j.ExtensionPoint;

/**
 * @Author foraixh
 * @Date 2022-07-24 16:39:00
 * @Version 1.0
 * @Usage usage
 */
public interface Greeting extends ExtensionPoint {
    String greet(String name);
}
