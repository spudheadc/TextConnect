package au.id.wattle.chapman.propertiesEditor;

import static com.google.common.collect.Lists.newArrayList;

import java.util.ArrayList;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.RequestMethod;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import au.id.wattle.chapman.propertiesEditor.model.ErrorDTO;
import au.id.wattle.chapman.propertiesEditor.service.PropertiesEditorServiceImpl;
import au.id.wattle.chapman.propertiesEditor.service.PropertyEditorService;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Predicates;

@SpringBootApplication
@EnableSwagger2
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Autowired(required=false)
	private ApplicationConfig config;

	@Bean
	public PropertyEditorService getPropertyEditorService(
			ResourceLoader resourceLoader) {
		PropertiesEditorServiceImpl pesi = new PropertiesEditorServiceImpl();
		ArrayList<Resource> resources = new ArrayList<Resource>();

		for (Iterator<String> iterator = config.getFiles().iterator(); iterator
				.hasNext();) {
			String filePath = iterator.next();

			resources.add(new FileSystemResource(filePath));

		}
		pesi.setFiles(resources);
		return pesi;
	}

	@Bean
	public Docket cbibApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.apis(Predicates.not(RequestHandlerSelectors
						.basePackage("org.springframework.boot")))
				.build()
				.pathMapping("/")
				.globalResponseMessage(
						RequestMethod.GET,
						newArrayList(
								new ResponseMessageBuilder()
										.code(500)
										.message("Internal Error")
										.responseModel(new ModelRef("ErrorDTO"))
										.build(),
								new ResponseMessageBuilder()
										.code(404)
										.message("Invalid ID")
										.responseModel(new ModelRef("ErrorDTO"))
										.build())).enableUrlTemplating(false)
				.tags(new Tag("NABC Apis", "All apis relating to nabc"))
				.additionalModels(typeResolver.resolve(ErrorDTO.class));
	}

	@Autowired
	private TypeResolver typeResolver;

	@Bean
	UiConfiguration uiConfig() {
		return new UiConfiguration("validatorUrl",// url
				"none", // docExpansion => none | list
				"alpha", // apiSorter => alpha
				"schema", // defaultModelRendering => schema
				UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS, false, // enableJsonEditor
																			// =>
																			// true
																			// |
																			// false
				true,null); // showRequestHeaders => true | false
	}

}
