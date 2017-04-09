package au.id.wattle.chapman.propertiesEditor.controllers;

import io.swagger.annotations.ApiOperation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import au.id.wattle.chapman.propertiesEditor.model.ErrorDTO;
import au.id.wattle.chapman.propertiesEditor.model.TreeNode;
import au.id.wattle.chapman.propertiesEditor.service.PropertyEditorService;

@RestController
public class PropertiesEditorController {

	@Autowired
	private PropertyEditorService service;

	@RequestMapping(method = RequestMethod.GET, value = "/messages/resources")

	@ApiOperation(value = "Reads the configured properties files and returns a tree structure. This "
			+ "operation will parse the properties file to return a structure based on a dot notation"
			+ "used to separate nodes.")
	public List<TreeNode> getValues() {
		return service.getTree();
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/messages/resources")

	@ApiOperation(value = "Writes the configured properties files with the supplied tree structure. This "
			+ "operation will parse the properties file to return a structure based on a dot notation"
			+ "used to separate nodes.")
	public List<TreeNode> putValues(@RequestBody List<TreeNode> node) {

		return service.setTree(node);
	}

	// Specify name of a specific view that will be used to display the error:
	@ExceptionHandler({ Exception.class })
	private ErrorDTO error(Exception e) {
		ErrorDTO error = new ErrorDTO();
		error.setErrorMessage(e.getMessage());

		return error;
	}

}
