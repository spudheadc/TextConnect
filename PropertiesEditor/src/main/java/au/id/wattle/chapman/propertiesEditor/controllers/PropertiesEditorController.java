package au.id.wattle.chapman.propertiesEditor.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import au.id.wattle.chapman.propertiesEditor.model.TreeNode;
import au.id.wattle.chapman.propertiesEditor.service.PropertyEditorService;

@RestController
public class PropertiesEditorController {

	@Autowired
	private PropertyEditorService service;

	@RequestMapping(method = RequestMethod.GET, value = "/editableProperties")
	public List<TreeNode> getValues() {
		return service.getTree();
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/editableProperties")
	public List<TreeNode> putValues(@RequestBody List<TreeNode> node) {

		return service.setTree(node);
	}

}
