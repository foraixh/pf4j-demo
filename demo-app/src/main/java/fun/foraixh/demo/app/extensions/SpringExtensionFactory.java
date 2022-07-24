package fun.foraixh.demo.app.extensions;

import org.pf4j.ExtensionFactory;
import org.pf4j.PluginManager;
import org.springframework.context.ApplicationContext;

/**
 * @Author foraixh
 * @Date 2022-07-24 18:32:00
 * @Version 1.0
 * @Usage usage
 */
public class SpringExtensionFactory implements ExtensionFactory {
    private ApplicationContext applicationContext;

    public SpringExtensionFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public <T> T create(Class<T> extensionClass) {
        return applicationContext.getBean(extensionClass);
    }
}
