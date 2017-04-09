package au.id.wattle.chapman.propertiesEditor;

import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import au.id.wattle.chapman.propertiesEditor.service.PropertiesEditorServiceImpl;
import au.id.wattle.chapman.propertiesEditor.service.PropertyEditorService;

@SpringBootApplication
public class Application {
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public PropertyEditorService getPropertyEditorService(ResourceLoader resourceLoader) {
		PropertiesEditorServiceImpl pesi = new PropertiesEditorServiceImpl();
		ArrayList<Resource> resources = new ArrayList<Resource>();
	
		resources.add(new FileSystemResource("/home/chris/abc.properties"));
		pesi.setFiles(resources);
		return pesi;
	}
}
