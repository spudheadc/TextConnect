package au.id.wattle.chapman.propertiesEditor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="textConnect")
public class ApplicationConfig {

    private List<String> files = new ArrayList<String>();

    public List<String> getFiles() {
        return this.files;
    }
}
