package uwu.narumi.lolibunker.logger;

import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;
import uwu.narumi.lolibunker.LoliBunker;

import java.io.Serializable;

@Plugin(name = "InMemoryAppender", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE, printObject = true)
public class InMemoryAppender extends AbstractAppender {

    public InMemoryAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, Property[] properties) {
        super(name, filter, layout, ignoreExceptions, properties);
    }

    @PluginFactory
    public static InMemoryAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Layout") Layout<? extends Serializable> layout,
            @PluginElement("Filter") final Filter filter
    ) {
        if (name == null)
            throw new IllegalArgumentException("No name provided for MyCustomAppenderImpl");

        return new InMemoryAppender(name, filter, layout == null ? PatternLayout.createDefaultLayout() : layout, true, null);
    }

    @Override
    public void append(LogEvent logEvent) {
        LoliBunker.INSTANCE.getLogs().add(logEvent.getMessage().getFormattedMessage());
    }
}
